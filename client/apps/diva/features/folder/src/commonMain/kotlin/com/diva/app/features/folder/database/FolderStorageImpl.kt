package com.diva.app.features.folder.database

import com.diva.app.database.DivaDB
import com.diva.app.database.folder.FolderStorage
import com.diva.app.models.collection.VisibilityType
import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media
import com.diva.app.models.media.MediaType
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class FolderStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : FolderStorage {

    override suspend fun getByUser(userId: Uuid): Result<List<Folder>> {
        return db.getList { folderQueries.findByUser(userId.toString(), ::mapToFolder) }
    }

    override fun getByUserFlow(userId: Uuid): Flow<Result<List<Folder>>> {
        return db.getListAsFlow { folderQueries.findByUser(userId.toString(), ::mapToFolder) }
    }

    override suspend fun getById(id: Uuid): Result<Option<Folder>> {
        return db.getOne { folderQueries.findById(id.toString(), ::mapToFolder) }
    }

    override fun getByIdFlow(id: Uuid): Flow<Result<Option<Folder>>> {
        return db.getOneAsFlow { folderQueries.findById(id.toString(), ::mapToFolder) }
    }

    override suspend fun getRoots(userId: Uuid): Result<List<Folder>> {
        return db.getList { folderQueries.findRoots(userId.toString(), ::mapToFolder) }
    }

    override fun getRootsFlow(userId: Uuid): Flow<Result<List<Folder>>> {
        return db.getListAsFlow { folderQueries.findRoots(userId.toString(), ::mapToFolder) }
    }

    override suspend fun getChildren(userId: Uuid, parentId: Uuid): Result<List<Folder>> {
        return db.getList { folderQueries.findChildren(userId.toString(), parentId.toString(), ::mapToFolder) }
    }

    override suspend fun getMediaByFolder(folderId: Uuid): Result<List<Media>> {
        return db.getList { folderQueries.findFolderMedia(folderId.toString(), ::mapToMedia) }
    }

    override suspend fun upsert(item: Folder): Result<Unit> {
        return db.use {
            transaction {
                folderQueries.upsert(
                    id = item.id.toString(),
                    user_id = item.userId.toString(),
                    name = item.name,
                    path = item.path,
                    parent_id = item.parentId.map { it.toString() }.getOrNull(),
                    scanned_at = item.scannedAt.epochSeconds,
                    created_at = item.createdAt.epochSeconds,
                    updated_at = item.updatedAt.epochSeconds,
                )
            }
        }
    }

    override suspend fun delete(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                folderQueries.deleteById(id.toString())
            }
        }
    }

    override suspend fun deleteAllByUser(userId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                folderQueries.deleteAllByUser(userId.toString())
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
        id = Uuid.parse(id),
        userId = Uuid.parse(userId),
        name = name,
        path = path,
        parentId = Option.of(parentId?.let { Uuid.parse(it) }),
        scannedAt = Instant.fromEpochSeconds(scannedAt),
        createdAt = Instant.fromEpochSeconds(createdAt),
        updatedAt = Instant.fromEpochSeconds(updatedAt),
        deletedAt = Option.of(deletedAt?.let { Instant.fromEpochSeconds(it) }),
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
        id = Uuid.parse(id),
        submittedBy = User(id = Uuid.parse(submittedBy)),
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
        publishedAt = Instant.fromEpochSeconds(publishedAt),
        fingerprint = Option.of(fingerprint),
        createdAt = Instant.fromEpochSeconds(createdAt),
        updatedAt = Instant.fromEpochSeconds(updatedAt),
        deletedAt = Option.of(deletedAt?.let { Instant.fromEpochSeconds(it) }),
    )
}