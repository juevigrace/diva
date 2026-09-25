package com.diva.app.folder.database

import io.github.juevigrace.diva.database.DivaDatabase
import kotlin.time.Clock

interface MediaFolderLinkStorage {
    suspend fun link(mediaId: String, folderId: String): Result<Unit>

    suspend fun unlink(mediaId: String, folderId: String): Result<Unit>

    suspend fun removeAllByMedia(mediaId: String): Result<Unit>

    suspend fun removeAllByFolder(folderId: String): Result<Unit>
}

class MediaFolderLinkStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : MediaFolderLinkStorage {

    override suspend fun link(mediaId: String, folderId: String): Result<Unit> {
        return db.use {
            transaction {
                mediaFolderLinkQueries.link(
                    media_id = mediaId,
                    folder_id = folderId,
                    created_at = Clock.System.now().epochSeconds,
                )
            }
        }
    }

    override suspend fun unlink(mediaId: String, folderId: String): Result<Unit> {
        return db.use {
            transaction {
                mediaFolderLinkQueries.unlink(mediaId, folderId)
            }
        }
    }

    override suspend fun removeAllByMedia(mediaId: String): Result<Unit> {
        return db.use {
            transaction {
                mediaFolderLinkQueries.deleteMediaLinks(mediaId)
            }
        }
    }

    override suspend fun removeAllByFolder(folderId: String): Result<Unit> {
        return db.use {
            transaction {
                mediaFolderLinkQueries.deleteFolderLinks(folderId)
            }
        }
    }
}
