package com.diva.app.file.database

import com.diva.app.file.models.DeviceFile
import com.diva.app.file.models.Directory
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow

interface DirectoryStorage {
    suspend fun getAll(): Result<List<Directory>>

    fun getAllFlow(): Flow<Result<List<Directory>>>

    suspend fun getById(id: String): Result<Option<Directory>>

    fun getByIdFlow(id: String): Flow<Result<Option<Directory>>>

    suspend fun getRoots(): Result<List<Directory>>

    fun getRootsFlow(): Flow<Result<List<Directory>>>

    /** Contents of the platform roots; what the files tab shows at its top level. */
    fun getRootChildrenFlow(): Flow<Result<List<Directory>>>

    suspend fun getChildren(parentId: String): Result<List<Directory>>

    fun getChildrenFlow(parentId: String): Flow<Result<List<Directory>>>

    suspend fun getFiles(parentId: String): Result<List<DeviceFile>>

    suspend fun upsert(item: Directory): Result<Unit>

    suspend fun upsertFile(item: DeviceFile): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>

    suspend fun deleteStaleChildren(parentId: String?, scannedBefore: Long): Result<Unit>
}

class DirectoryStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : DirectoryStorage {

    override suspend fun getAll(): Result<List<Directory>> {
        return db.getList { directoryQueries.findAll(::mapToDirectory) }
    }

    override fun getAllFlow(): Flow<Result<List<Directory>>> {
        return db.getListAsFlow { directoryQueries.findAll(::mapToDirectory) }
    }

    override suspend fun getById(id: String): Result<Option<Directory>> {
        return db.getOne { directoryQueries.findById(id, ::mapToDirectory) }
    }

    override fun getByIdFlow(id: String): Flow<Result<Option<Directory>>> {
        return db.getOneAsFlow { directoryQueries.findById(id, ::mapToDirectory) }
    }

    override suspend fun getRoots(): Result<List<Directory>> {
        return db.getList { directoryQueries.findRoots(::mapToDirectory) }
    }

    override fun getRootsFlow(): Flow<Result<List<Directory>>> {
        return db.getListAsFlow { directoryQueries.findRoots(::mapToDirectory) }
    }

    override fun getRootChildrenFlow(): Flow<Result<List<Directory>>> {
        return db.getListAsFlow { directoryQueries.findRootChildren(::mapToDirectory) }
    }

    override suspend fun getChildren(parentId: String): Result<List<Directory>> {
        return db.getList { directoryQueries.findChildren(parentId, ::mapToDirectory) }
    }

    override fun getChildrenFlow(parentId: String): Flow<Result<List<Directory>>> {
        return db.getListAsFlow { directoryQueries.findChildren(parentId, ::mapToDirectory) }
    }

    override suspend fun getFiles(parentId: String): Result<List<DeviceFile>> {
        return db.getList { directoryQueries.findFileChildren(parentId, ::mapToDeviceFile) }
    }

    override suspend fun upsert(item: Directory): Result<Unit> {
        return db.use {
            transaction {
                val existingId = directoryQueries.findIdByPath(item.path).executeAsOneOrNull()
                if (existingId != null) {
                    directoryQueries.updateAfterScan(
                        name = item.name,
                        parent_id = item.parent.getOrNull()?.id,
                        is_directory = 1L,
                        mime_type = null,
                        size_bytes = null,
                        last_modified = null,
                        scanned_at = item.scannedAt,
                        updated_at = item.updatedAt,
                        deleted_at = item.deletedAt.getOrNull(),
                        id = existingId,
                    )
                } else {
                    directoryQueries.insert(
                        id = item.id,
                        name = item.name,
                        path = item.path,
                        parent_id = item.parent.getOrNull()?.id,
                        is_directory = 1L,
                        mime_type = null,
                        size_bytes = null,
                        last_modified = null,
                        scanned_at = item.scannedAt,
                        created_at = item.createdAt,
                        updated_at = item.updatedAt,
                        deleted_at = item.deletedAt.getOrNull(),
                    )
                }
            }
        }
    }

    override suspend fun upsertFile(item: DeviceFile): Result<Unit> {
        return db.use {
            transaction {
                val parentId = item.parent.getOrNull()?.id
                    ?: error("DeviceFile ${item.path} has no parent directory")
                val existingId = directoryQueries.findIdByPath(item.path).executeAsOneOrNull()
                if (existingId != null) {
                    directoryQueries.updateAfterScan(
                        name = item.name,
                        parent_id = parentId,
                        is_directory = 0L,
                        mime_type = item.mimeType.getOrNull(),
                        size_bytes = item.sizeBytes,
                        last_modified = item.lastModified,
                        scanned_at = item.scannedAt,
                        updated_at = item.updatedAt,
                        deleted_at = item.deletedAt.getOrNull(),
                        id = existingId,
                    )
                } else {
                    directoryQueries.insert(
                        id = item.id,
                        name = item.name,
                        path = item.path,
                        parent_id = parentId,
                        is_directory = 0L,
                        mime_type = item.mimeType.getOrNull(),
                        size_bytes = item.sizeBytes,
                        last_modified = item.lastModified,
                        scanned_at = item.scannedAt,
                        created_at = item.createdAt,
                        updated_at = item.updatedAt,
                        deleted_at = item.deletedAt.getOrNull(),
                    )
                }
            }
        }
    }

    override suspend fun delete(id: String): Result<Unit> {
        return db.use {
            transaction {
                directoryQueries.deleteById(id)
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                directoryQueries.deleteAll()
            }
        }
    }

    override suspend fun deleteStaleChildren(parentId: String?, scannedBefore: Long): Result<Unit> {
        return db.use {
            transaction {
                directoryQueries.deleteStaleChildren(parentId, scannedBefore)
            }
        }
    }

    private fun mapToDirectory(
        id: String,
        name: String,
        path: String,
        parentId: String?,
        isDirectory: Long,
        mimeType: String?,
        sizeBytes: Long?,
        lastModified: Long?,
        scannedAt: Long,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Directory = Directory(
        id = id,
        name = name,
        path = path,
        parent = Option.of(parentId?.let { Directory(id = it, name = "", path = "") }),
        scannedAt = scannedAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = Option.of(deletedAt),
    )

    private fun mapToDeviceFile(
        id: String,
        name: String,
        path: String,
        parentId: String?,
        isDirectory: Long,
        mimeType: String?,
        sizeBytes: Long?,
        lastModified: Long?,
        scannedAt: Long,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): DeviceFile = DeviceFile(
        id = id,
        name = name,
        path = path,
        parent = Option.of(Directory(id = parentId ?: error("File $path has no parent"), name = "", path = "")),
        mimeType = Option.of(mimeType),
        sizeBytes = sizeBytes ?: 0L,
        lastModified = lastModified ?: 0L,
        scannedAt = scannedAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = Option.of(deletedAt),
    )
}
