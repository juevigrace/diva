package com.diva.app.file.data

import com.diva.app.file.database.DirectoryStorage
import com.diva.app.file.domain.DirectoryRepository
import com.diva.app.file.domain.services.FileSystemService
import com.diva.app.file.models.DeviceFile
import com.diva.app.file.models.Directory
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

/**
 * Device-global directory/file cache backed by [DirectoryStorage]. Reads are
 * served from the database; [refresh] pulls a fresh listing for one directory
 * (or the roots) from the platform [FileSystemService] and prunes whatever the
 * scan no longer saw.
 */
class DirectoryRepositoryImpl(
    private val storage: DirectoryStorage,
    private val fileSystem: FileSystemService,
) : DirectoryRepository {

    override fun observe(): Flow<Result<List<Directory>>> = storage.getAllFlow()

    override fun observe(id: String): Flow<Result<Option<Directory>>> = storage.getByIdFlow(id)

    override fun observeRoots(): Flow<Result<List<Directory>>> = storage.getRootsFlow()

    override fun observeChildren(directoryId: String?): Flow<Result<List<Directory>>> =
        if (directoryId == null) storage.getRootChildrenFlow() else storage.getChildrenFlow(directoryId)

    override suspend fun getChildren(parentId: String): Result<List<Directory>> =
        storage.getChildren(parentId)

    override suspend fun getFiles(parentId: String): Result<List<DeviceFile>> =
        storage.getFiles(parentId)

    override suspend fun breadcrumbs(directoryId: String?): List<Directory> {
        if (directoryId == null) return emptyList()
        val trail = ArrayDeque<Directory>()
        var currentId: String? = directoryId
        while (currentId != null) {
            val directory = storage.getById(currentId).getOrThrow().getOrNull() ?: break
            trail.addFirst(directory)
            currentId = directory.parent.getOrNull()?.id
        }
        return trail.toList()
    }

    override suspend fun refresh(parentId: String?): Result<Unit> = runCatching {
        val scanStart = Clock.System.now().toEpochMilliseconds()
        val parents: List<Directory> = if (parentId == null) {
            fileSystem.roots().onEach { root ->
                storage.upsert(root.copy(parent = None, scannedAt = scanStart)).getOrThrow()
            }
        } else {
            listOf(storage.getById(parentId).getOrThrow().getOrNull() ?: error("Directory $parentId not found"))
        }
        parents.forEach { parent ->
            val parentStub = Directory(id = parent.id, name = parent.name, path = parent.path)
            val listing = fileSystem.listChildren(parent)
            listing.directories.forEach { dir ->
                storage.upsert(dir.copy(parent = Option.of(parentStub), scannedAt = scanStart)).getOrThrow()
            }
            listing.files.forEach { file ->
                storage.upsertFile(file.copy(parent = Option.of(parentStub), scannedAt = scanStart)).getOrThrow()
            }
            storage.deleteStaleChildren(parent.id, scanStart).getOrThrow()
        }
        if (parentId == null) {
            storage.deleteStaleChildren(null, scanStart).getOrThrow()
        }
    }

    override suspend fun upsert(directory: Directory): Result<Unit> = storage.upsert(directory)

    override suspend fun delete(id: String): Result<Unit> = storage.delete(id)

    override suspend fun deleteAll(): Result<Unit> = storage.deleteAll()
}
