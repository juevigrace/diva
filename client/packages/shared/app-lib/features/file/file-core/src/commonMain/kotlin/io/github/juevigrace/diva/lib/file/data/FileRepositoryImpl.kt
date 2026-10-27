package io.github.juevigrace.diva.lib.file.data

import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.ioDispatcher
import io.github.juevigrace.diva.lib.file.database.EntryStorage
import io.github.juevigrace.diva.lib.file.domain.FileRepository
import io.github.juevigrace.diva.lib.file.domain.services.FileSystemService
import io.github.juevigrace.diva.lib.file.models.DeviceFile
import io.github.juevigrace.diva.lib.file.models.Directory
import io.github.juevigrace.diva.lib.file.models.EntryRef
import io.github.juevigrace.diva.lib.file.models.FsEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
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

    private val refreshMutexes = mutableMapOf<String?, Mutex>()
    private val refreshMutexesGuard = Mutex()
    private val lastRefreshAt = mutableMapOf<String?, Long>()
    private val lastRefreshGuard = Mutex()

    private suspend fun mutexFor(parentId: String?): Mutex = refreshMutexesGuard.withLock {
        refreshMutexes.getOrPut(parentId) { Mutex() }
    }

    override fun observe(): Flow<Result<List<FsEntry>>> = storage.getAllFlow()

    override fun observeRoot(): Flow<Result<List<FsEntry>>> =
        storage.getByParentFlow()
            .onStart {
                scope.launch {
                    refreshIfStale(null)
                }
            }

    override fun observeDir(id: String): Flow<Result<List<FsEntry>>> =
        storage.getByParentFlow(id)
            .onStart {
                scope.launch {
                    refreshIfStale(id)
                }
            }

    override suspend fun refresh(parentId: String?): Result<Unit> = withContext(ioDispatcher) {
        mutexFor(parentId).withLock {
            try {
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
                lastRefreshGuard.withLock {
                    lastRefreshAt[parentId] = Clock.System.now().toEpochMilliseconds()
                }
                Result.success(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
    }

    private suspend fun refreshIfStale(parentId: String?, ttlMs: Long = 5000L, force: Boolean = false): Result<Unit> {
        if (!force) {
            val last = lastRefreshGuard.withLock { lastRefreshAt[parentId] }
            val now = Clock.System.now().toEpochMilliseconds()
            if (last != null && now - last < ttlMs) {
                return Result.success(Unit)
            }
        }
        return refresh(parentId)
    }

    override suspend fun refreshCurrent(parentId: String?): Result<Unit> {
        return refreshIfStale(parentId, force = true)
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
