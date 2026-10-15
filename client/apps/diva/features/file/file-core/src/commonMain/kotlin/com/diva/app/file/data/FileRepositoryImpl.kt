package com.diva.app.file.data

import com.diva.app.file.database.EntryStorage
import com.diva.app.file.domain.FileRepository
import com.diva.app.file.domain.services.FileSystemService
import com.diva.app.file.models.DeviceFile
import com.diva.app.file.models.Directory
import com.diva.app.file.models.EntryRef
import com.diva.app.file.models.FsEntry
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

/**
 * Device-global directory/file cache backed by [EntryStorage]. Reads are served
 * from the database; [refresh] reads a single directory (or the root) from the
 * platform [FileSystemService], upserts it and its children, and prunes cached
 * children that no longer exist on disk.
 */
class FileRepositoryImpl(
    private val entries: EntryStorage,
    private val fileSystem: FileSystemService,
) : FileRepository {

    override fun observe(): Flow<Result<List<FsEntry>>> = entries.getAllFlow()

    override suspend fun breadcrumbs(directoryId: String?): List<Directory> {
        if (directoryId == null) return emptyList()
        val trail = ArrayDeque<Directory>()
        var currentId: String? = directoryId
        while (currentId != null) {
            val directory = entries.getById(currentId).getOrThrow().getOrNull() as? Directory ?: break
            if (directory.parent.getOrNull() == null) break
            trail.addFirst(directory)
            currentId = directory.parent.getOrNull()?.id
        }
        return trail.toList()
    }

    override suspend fun refresh(parentId: String?): Result<Unit> = runCatching {
        val scanStart = Clock.System.now().toEpochMilliseconds()

        if (parentId == null) {
            fileSystem.roots().getOrThrow().forEach { root ->
                entries.upsert(
                    root.copy(meta = root.meta.copy(parent = None, scannedAt = scanStart)),
                ).getOrThrow()
            }
            return@runCatching
        }

        val parent = entries.getById(parentId).getOrThrow().getOrNull() as? Directory
            ?: error("Directory $parentId not found")

        val scanned = fileSystem.read(parent.path).getOrThrow()
        val directoryId = entries.upsert(
            scanned.copy(meta = scanned.meta.copy(parent = parent.parent, scannedAt = scanStart)),
        ).getOrThrow()

        val childParent = Option.of(EntryRef(directoryId, scanned.path, scanned.name))
        scanned.entries.forEach { child ->
            val updated = when (child) {
                is Directory -> child.copy(meta = child.meta.copy(parent = childParent, scannedAt = scanStart))
                is DeviceFile -> child.copy(meta = child.meta.copy(parent = childParent, scannedAt = scanStart))
            }
            entries.upsert(updated).getOrThrow()
        }

        entries.deleteStaleChildren(directoryId, scanStart).getOrThrow()
    }
}
