package com.diva.app.file.database

import com.diva.app.file.models.DeviceFile
import com.diva.app.file.models.Directory
import com.diva.app.file.models.EntryMeta
import com.diva.app.file.models.EntryRef
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow

interface FileStorage {
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

class FileStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : FileStorage {

    override suspend fun getAll(): Result<List<Directory>> {
        return db.getList { entryQueries.findAll(::mapToDirectory) }
    }

    override fun getAllFlow(): Flow<Result<List<Directory>>> {
        return db.getListAsFlow { entryQueries.findAll(::mapToDirectory) }
    }

    override suspend fun getById(id: String): Result<Option<Directory>> {
        return db.getOne { entryQueries.findById(id, ::mapToDirectory) }
    }

    override fun getByIdFlow(id: String): Flow<Result<Option<Directory>>> {
        return db.getOneAsFlow { entryQueries.findById(id, ::mapToDirectory) }
    }

    override suspend fun getRoots(): Result<List<Directory>> {
        return db.getList { entryQueries.findRoots(::mapToRoot) }
    }

    override fun getRootsFlow(): Flow<Result<List<Directory>>> {
        return db.getListAsFlow { entryQueries.findRoots(::mapToRoot) }
    }

    override fun getRootChildrenFlow(): Flow<Result<List<Directory>>> {
        return db.getListAsFlow { entryQueries.findRootChildren(::mapToDirectory) }
    }

    override suspend fun getChildren(parentId: String): Result<List<Directory>> {
        return db.getList { entryQueries.findChildren(parentId, ::mapToDirectory) }
    }

    override fun getChildrenFlow(parentId: String): Flow<Result<List<Directory>>> {
        return db.getListAsFlow { entryQueries.findChildren(parentId, ::mapToDirectory) }
    }

    override suspend fun getFiles(parentId: String): Result<List<DeviceFile>> {
        return db.getList { entryQueries.findFiles(parentId, ::mapToFile) }
    }

    override suspend fun upsert(item: Directory): Result<Unit> {
        return db.use {
            transaction {
                val meta = item.meta
                val parentId = meta.parent.getOrNull()?.id
                val existingId = entryQueries.findIdByPath(meta.path).executeAsOneOrNull()
                if (existingId != null) {
                    entryQueries.updateEntryAfterScan(
                        name = meta.name,
                        parent_id = parentId,
                        last_modified = meta.lastModified,
                        scanned_at = meta.scannedAt,
                        updated_at = meta.updatedAt,
                        deleted_at = meta.deletedAt.getOrNull(),
                        id = existingId,
                    )
                    // A path that used to hold a file now holds a directory.
                    fileQueries.deleteFileById(existingId)
                } else {
                    entryQueries.insertEntry(
                        id = meta.id,
                        name = meta.name,
                        path = meta.path,
                        parent_id = parentId,
                        last_modified = meta.lastModified,
                        scanned_at = meta.scannedAt,
                        created_at = meta.createdAt,
                        updated_at = meta.updatedAt,
                        deleted_at = meta.deletedAt.getOrNull(),
                    )
                }
            }
        }
    }

    override suspend fun upsertFile(item: DeviceFile): Result<Unit> {
        return db.use {
            transaction {
                val meta = item.meta
                val parentId = meta.parent.getOrNull()?.id
                    ?: error("DeviceFile ${meta.path} has no parent directory")
                val mimeType = item.mimeType.getOrNull()
                val existingId = entryQueries.findIdByPath(meta.path).executeAsOneOrNull()
                if (existingId != null) {
                    entryQueries.updateEntryAfterScan(
                        name = meta.name,
                        parent_id = parentId,
                        last_modified = meta.lastModified,
                        scanned_at = meta.scannedAt,
                        updated_at = meta.updatedAt,
                        deleted_at = meta.deletedAt.getOrNull(),
                        id = existingId,
                    )
                } else {
                    entryQueries.insertEntry(
                        id = meta.id,
                        name = meta.name,
                        path = meta.path,
                        parent_id = parentId,
                        last_modified = meta.lastModified,
                        scanned_at = meta.scannedAt,
                        created_at = meta.createdAt,
                        updated_at = meta.updatedAt,
                        deleted_at = meta.deletedAt.getOrNull(),
                    )
                }
                val entryId = existingId ?: meta.id
                val updated = fileQueries.updateFile(
                    size_bytes = item.sizeBytes,
                    mime_type = mimeType,
                    dir_id = entryId,
                )
                if (updated == 0L) {
                    fileQueries.insertFile(
                        dir_id = entryId,
                        size_bytes = item.sizeBytes,
                        mime_type = mimeType,
                    )
                }
            }
        }
    }

    override suspend fun delete(id: String): Result<Unit> {
        return db.use {
            transaction {
                entryQueries.deleteById(id)
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                entryQueries.deleteAll()
            }
        }
    }

    override suspend fun deleteStaleChildren(parentId: String?, scannedBefore: Long): Result<Unit> {
        return db.use {
            transaction {
                entryQueries.deleteStaleChildren(parentId, scannedBefore)
            }
        }
    }

    private fun mapToDirectory(
        id: String,
        name: String,
        path: String,
        parentId: String?,
        lastModified: Long,
        scannedAt: Long,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
        parentPath: String?,
        parentName: String?,
    ): Directory = Directory(
        meta = toMeta(
            id = id,
            name = name,
            path = path,
            parentId = parentId,
            lastModified = lastModified,
            scannedAt = scannedAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
            parentPath = parentPath,
            parentName = parentName,
        ),
    )

    /** Roots have no parent row by definition, so the query omits parent columns. */
    private fun mapToRoot(
        id: String,
        name: String,
        path: String,
        parentId: String?,
        lastModified: Long,
        scannedAt: Long,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Directory = Directory(
        meta = toMeta(
            id = id,
            name = name,
            path = path,
            parentId = parentId,
            lastModified = lastModified,
            scannedAt = scannedAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
        ),
    )

    private fun mapToFile(
        id: String,
        name: String,
        path: String,
        parentId: String?,
        lastModified: Long,
        scannedAt: Long,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
        parentPath: String?,
        parentName: String?,
        sizeBytes: Long,
        mimeType: String?,
    ): DeviceFile = DeviceFile(
        meta = toMeta(
            id = id,
            name = name,
            path = path,
            parentId = parentId,
            lastModified = lastModified,
            scannedAt = scannedAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
            parentPath = parentPath,
            parentName = parentName,
        ),
        sizeBytes = sizeBytes,
        mimeType = Option.of(mimeType),
    )

    private fun toMeta(
        id: String,
        name: String,
        path: String,
        parentId: String?,
        lastModified: Long,
        scannedAt: Long,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
        parentPath: String? = null,
        parentName: String? = null,
    ): EntryMeta = EntryMeta(
        id = id,
        name = name,
        path = path,
        lastModified = lastModified,
        parent = Option.of(
            parentId?.let { EntryRef(id = it, path = parentPath.orEmpty(), name = parentName.orEmpty()) },
        ),
        scannedAt = scannedAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = Option.of(deletedAt),
    )
}
