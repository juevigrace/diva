package io.github.juevigrace.diva.lib.file.database

import io.github.juevigrace.diva.lib.database.file.DivaSharedDB
import io.github.juevigrace.diva.lib.file.models.DeviceFile
import io.github.juevigrace.diva.lib.file.models.Directory
import io.github.juevigrace.diva.lib.file.models.EntryMeta
import io.github.juevigrace.diva.lib.file.models.EntryRef
import io.github.juevigrace.diva.lib.file.models.FileCategory
import io.github.juevigrace.diva.lib.file.models.FsEntry
import io.github.juevigrace.diva.lib.file.models.MimeType
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.getOrThrow
import kotlin.time.Clock
import kotlin.uuid.Uuid

interface EntryStorage {
    suspend fun getAll(): Result<List<FsEntry>>

    fun getAllFlow(): Flow<Result<List<FsEntry>>>

    suspend fun getById(id: String): Result<Option<FsEntry>>

    fun getByIdFlow(id: String): Flow<Result<Option<FsEntry>>>

    suspend fun getByParent(parentId: String? = null): Result<List<FsEntry>>

    fun getByParentFlow(parentId: String? = null): Flow<Result<List<FsEntry>>>

    suspend fun upsert(entry: FsEntry): Result<String>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>

    suspend fun deleteStaleChildren(parentId: String?, scannedBefore: Long): Result<Unit>

    suspend fun deleteFile(id: String): Result<Unit>

    suspend fun deleteAllFiles(): Result<Unit>
}

class EntryStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>,
) : EntryStorage {

    override suspend fun getAll(): Result<List<FsEntry>> {
        return db.getList { entryQueries.findAll(::mapToEntry) }
    }

    override fun getAllFlow(): Flow<Result<List<FsEntry>>> {
        return db.getListAsFlow { entryQueries.findAll(::mapToEntry) }
    }

    override suspend fun getById(id: String): Result<Option<FsEntry>> {
        return db.getOne { entryQueries.findById(id, ::mapToEntry) }
    }

    override fun getByIdFlow(id: String): Flow<Result<Option<FsEntry>>> {
        return db.getOneAsFlow { entryQueries.findById(id, ::mapToEntry) }
    }

    private suspend fun getByPath(path: String): Result<Option<String>> {
        return db.getOne { entryQueries.findByPath(path) }
    }

    override suspend fun getByParent(parentId: String?): Result<List<FsEntry>> {
        return db.getList { entryQueries.findByParent(parentId, ::mapToEntry) }
    }

    override fun getByParentFlow(parentId: String?): Flow<Result<List<FsEntry>>> {
        return db.getListAsFlow { entryQueries.findByParent(parentId, ::mapToEntry) }
    }

    override suspend fun upsert(entry: FsEntry): Result<String> {
        return db.use {
            transactionWithResult {
                val id = getByPath(entry.path).getOrThrow().fold(
                    onNone = {
                        val id = Uuid.random().toString()
                        entryQueries.insert(
                            id = id,
                            name = entry.name,
                            path = entry.path,
                            parent_id = entry.parent.getOrNull()?.id,
                            last_modified = entry.lastModified,
                            hidden = entry.hidden,
                            scanned_at = entry.scannedAt,
                        )
                        id
                    },
                    onSome = { id ->
                        entryQueries.update(
                            name = entry.name,
                            parent_id = entry.parent.getOrNull()?.id,
                            last_modified = entry.lastModified,
                            hidden = entry.hidden,
                            scanned_at = entry.scannedAt,
                            updated_at = Clock.System.now().toEpochMilliseconds(),
                            id = id,
                        )
                        id
                    }
                )
                when (entry) {
                    is Directory -> fileQueries.deleteOne(id)
                    is DeviceFile -> {
                        val category = FileCategory.fromMimeType(entry.mimeType.getOrNull())
                        val existingFile = fileQueries.findOne(id).executeAsOneOrNull().toOption()
                        existingFile.fold(
                            onNone = {
                                fileQueries.insert(
                                    dir_id = id,
                                    size_bytes = entry.sizeBytes,
                                    mime_type = entry.mimeType.getOrNull(),
                                    category = category,
                                    content_uri = entry.contentUri.getOrNull(),
                                )
                            },
                            onSome = {
                                fileQueries.update(
                                    size_bytes = entry.sizeBytes,
                                    mime_type = entry.mimeType.getOrNull(),
                                    category = category,
                                    content_uri = entry.contentUri.getOrNull(),
                                    dir_id = id,
                                )
                            },
                        )
                    }
                }
                id
            }
        }
    }

    override suspend fun delete(id: String): Result<Unit> {
        return db.use {
            transaction {
                entryQueries.deleteOne(id)
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                entryQueries.delete()
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

    override suspend fun deleteFile(id: String): Result<Unit> {
        return db.use {
            transaction {
                fileQueries.deleteOne(id)
            }
        }
    }

    override suspend fun deleteAllFiles(): Result<Unit> {
        return db.use {
            transaction {
                fileQueries.delete()
            }
        }
    }

    private fun mapToEntry(
        id: String,
        name: String,
        path: String,
        parentId: String?,
        lastModified: Long,
        hidden: Boolean,
        scannedAt: Long,
        createdAt: Long,
        updatedAt: Long,
        parentPath: String?,
        parentName: String?,
        sizeBytes: Long?,
        mimeType: MimeType?,
        category: FileCategory?,
        contentUri: String?,
    ): FsEntry {
        val meta = EntryMeta(
            id = id,
            name = name,
            path = path,
            lastModified = lastModified,
            hidden = hidden,
            parent = parentId?.let {
                Option.some(
                    EntryRef(
                        it,
                        parentPath.orEmpty(),
                        parentName.orEmpty()
                    )
                )
            } ?: None,
            scannedAt = scannedAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
        return if (sizeBytes != null) {
            DeviceFile(
                meta = meta,
                sizeBytes = sizeBytes,
                mimeType = mimeType.toOption(),
                category = category ?: FileCategory.OTHER,
                contentUri = contentUri.toOption(),
            )
        } else {
            Directory(meta = meta)
        }
    }
}
