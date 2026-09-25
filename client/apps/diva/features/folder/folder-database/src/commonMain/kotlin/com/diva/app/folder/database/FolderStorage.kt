package com.diva.app.folder.database

import com.diva.app.models.collection.VisibilityType
import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media
import com.diva.app.models.media.MediaType
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow

interface FolderStorage {
    suspend fun getByUser(userId: String): Result<List<Folder>>

    fun getByUserFlow(userId: String): Flow<Result<List<Folder>>>

    suspend fun getById(id: String): Result<Option<Folder>>

    fun getByIdFlow(id: String): Flow<Result<Option<Folder>>>

    suspend fun getRoots(userId: String): Result<List<Folder>>

    fun getRootsFlow(userId: String): Flow<Result<List<Folder>>>

    suspend fun getChildren(userId: String, parentId: String): Result<List<Folder>>

    suspend fun getMediaByFolder(folderId: String): Result<List<Media>>

    suspend fun upsert(item: Folder): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAllByUser(userId: String): Result<Unit>
}

class FolderStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : FolderStorage {

    override suspend fun getByUser(userId: String): Result<List<Folder>> {
        return db.getList { folderQueries.findByUser(userId, ::mapToFolder) }
    }

    override fun getByUserFlow(userId: String): Flow<Result<List<Folder>>> {
        return db.getListAsFlow { folderQueries.findByUser(userId, ::mapToFolder) }
    }

    override suspend fun getById(id: String): Result<Option<Folder>> {
        return db.getOne { folderQueries.findById(id, ::mapToFolder) }
    }

    override fun getByIdFlow(id: String): Flow<Result<Option<Folder>>> {
        return db.getOneAsFlow { folderQueries.findById(id, ::mapToFolder) }
    }

    override suspend fun getRoots(userId: String): Result<List<Folder>> {
        return db.getList { folderQueries.findRoots(userId, ::mapToFolder) }
    }

    override fun getRootsFlow(userId: String): Flow<Result<List<Folder>>> {
        return db.getListAsFlow { folderQueries.findRoots(userId, ::mapToFolder) }
    }

    override suspend fun getChildren(userId: String, parentId: String): Result<List<Folder>> {
        return db.getList { folderQueries.findChildren(userId, parentId, ::mapToFolder) }
    }

    override suspend fun getMediaByFolder(folderId: String): Result<List<Media>> {
        return db.getList { folderQueries.findFolderMedia(folderId, ::mapToMedia) }
    }

    override suspend fun upsert(item: Folder): Result<Unit> {
        return db.use {
            transaction {
                folderQueries.upsert(
                    id = item.id,
                    user_id = item.userId,
                    name = item.name,
                    path = item.path,
                    parent_id = item.parentId.getOrNull(),
                    scanned_at = item.scannedAt,
                    created_at = item.createdAt,
                    updated_at = item.updatedAt,
                )
            }
        }
    }

    override suspend fun delete(id: String): Result<Unit> {
        return db.use {
            transaction {
                folderQueries.deleteById(id)
            }
        }
    }

    override suspend fun deleteAllByUser(userId: String): Result<Unit> {
        return db.use {
            transaction {
                folderQueries.deleteAllByUser(userId)
            }
        }
    }

    private fun mapToFolder(
        id: String,
        name: String,
        path: String,
        parentId: String?,
        userId: String,
        scannedAt: Long,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Folder = Folder(
        id = id,
        userId = userId,
        name = name,
        path = path,
        parentId = Option.of(parentId),
        scannedAt = scannedAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = Option.of(deletedAt),
    )

    private fun mapToMedia(
        id: String,
        submittedBy: String,
        mediaType: MediaType,
        title: String,
        uri: String,
        mimeType: String,
        sizeBytes: Long,
        durationMs: Long?,
        width: Long,
        height: Long,
        altText: String,
        visibility: VisibilityType,
        sensitiveContent: Boolean,
        adultContent: Boolean,
        publishedAt: Long,
        fingerprint: String?,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Media = Media(
        id = id,
        submittedBy = User(id = submittedBy),
        mediaType = mediaType,
        title = title,
        uri = uri,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        durationMs = Option.of(durationMs),
        width = width.toInt(),
        height = height.toInt(),
        altText = altText,
        visibility = visibility,
        sensitiveContent = sensitiveContent,
        adultContent = adultContent,
        publishedAt = publishedAt,
        fingerprint = Option.of(fingerprint),
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = Option.of(deletedAt),
    )
}
