package io.github.juevigrace.diva.lib.file.data

import io.github.juevigrace.diva.lib.file.database.EntryStorage
import io.github.juevigrace.diva.lib.file.domain.FileRepository
import io.github.juevigrace.diva.lib.file.domain.services.FileSystemService
import io.github.juevigrace.diva.lib.file.models.DeviceFile
import io.github.juevigrace.diva.lib.file.models.Directory
import io.github.juevigrace.diva.lib.file.models.EntryRef
import io.github.juevigrace.diva.lib.file.models.FsEntry
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock

// TODO: fix
/**
 * Device-global directory/file cache backed by [EntryStorage]. Reads are served
 * from the database; [observeRoot]/[observeDir] rescan the collected level
 * through the platform [FileSystemService], upsert it and its children, prune
 * cached children that no longer exist on disk, then emit the live cache.
 */
class FileRepositoryImpl(
    private val storage: EntryStorage,
    private val fileSystem: FileSystemService,
) : FileRepository {

    override fun observe(): Flow<Result<List<FsEntry>>> = storage.getAllFlow()

    override fun observeRoot(): Flow<Result<List<FsEntry>>> = flow {
        refresh(null).onFailure { emit(Result.failure(it)) }
        emitAll(storage.getByParentFlow().map { result -> result.map { entries -> entries.filter { !it.hidden } } })
    }

    override fun observeDir(id: String): Flow<Result<List<FsEntry>>> = flow {
        refresh(id).onFailure { emit(Result.failure(it)) }
        emitAll(storage.getByParentFlow(id))
    }

    override suspend fun refresh(parentId: String?): Result<Unit> = try {
        val scanStart = Clock.System.now().toEpochMilliseconds()

        if (parentId == null) {
            fileSystem.roots().getOrThrow().forEach { root ->
                storage.upsert(
                    root.copy(meta = root.meta.copy(parent = None, scannedAt = scanStart)),
                ).getOrThrow()
            }
            storage.deleteStaleChildren(null, scanStart).getOrThrow()
        } else {
            val parent = storage.getById(parentId).getOrThrow().getOrNull() as? Directory
                ?: error("Directory $parentId not found")

            val scanned = fileSystem.read(parent.path).getOrThrow()
            val directoryId = storage.upsert(
                scanned.copy(meta = scanned.meta.copy(parent = parent.parent, scannedAt = scanStart)),
            ).getOrThrow()

            val childParent = Option.of(EntryRef(directoryId, scanned.path, scanned.name))
            scanned.entries.forEach { child ->
                val updated = when (child) {
                    is Directory -> child.copy(meta = child.meta.copy(parent = childParent, scannedAt = scanStart))
                    is DeviceFile -> child.copy(meta = child.meta.copy(parent = childParent, scannedAt = scanStart))
                }
                storage.upsert(updated).getOrThrow()
            }

            storage.deleteStaleChildren(directoryId, scanStart).getOrThrow()
        }
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }

    override suspend fun defaultTrail(): Result<List<Directory>> = try {
        val target = fileSystem.defaultPath().getOrThrow()
        if (target == null) {
            Result.success(emptyList())
        } else {
            refresh(null).getOrThrow()
            val root = storage.getByParent().getOrThrow()
                .filterIsInstance<Directory>()
                .firstOrNull { isUnder(target, it.path) }
                ?: return Result.success(emptyList())

            // Walk down one segment at a time, scanning each level so the next
            // one resolves; the final level is left for the screen to observe
            // so it is not scanned twice.
            val trail = mutableListOf(root)
            var current: Directory = root
            var scanned = true
            for (segment in target.removePrefix(root.path).split('/', '\\')
                .filter { it.isNotEmpty() }) {
                if (!scanned) refresh(current.id).getOrThrow()
                scanned = false
                val child = storage.getByParent(current.id).getOrThrow()
                    .filterIsInstance<Directory>()
                    .firstOrNull { it.name == segment } ?: break
                trail += child
                current = child
            }
            Result.success(trail.toList())
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

/** True when [child] is [parent] itself or sits below it, on either separator. */
private fun isUnder(child: String, parent: String): Boolean {
    if (child == parent) return true
    val prefix = parent.trimEnd('/', '\\')
    return child.startsWith("$prefix/") || child.startsWith("$prefix\\")
}
