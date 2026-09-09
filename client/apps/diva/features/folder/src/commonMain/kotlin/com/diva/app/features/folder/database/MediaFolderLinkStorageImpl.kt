package com.diva.app.features.folder.database

import com.diva.app.database.DivaDB
import com.diva.app.database.folder.MediaFolderLinkStorage
import io.github.juevigrace.diva.database.DivaDatabase
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MediaFolderLinkStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : MediaFolderLinkStorage {

    override suspend fun link(mediaId: Uuid, folderId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                mediaFolderLinkQueries.link(
                    media_id = mediaId.toString(),
                    folder_id = folderId.toString(),
                    created_at = Clock.System.now().epochSeconds,
                )
            }
        }
    }

    override suspend fun unlink(mediaId: Uuid, folderId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                mediaFolderLinkQueries.unlink(mediaId.toString(), folderId.toString())
            }
        }
    }

    override suspend fun removeAllByMedia(mediaId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                mediaFolderLinkQueries.deleteMediaLinks(mediaId.toString())
            }
        }
    }

    override suspend fun removeAllByFolder(folderId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                mediaFolderLinkQueries.deleteFolderLinks(folderId.toString())
            }
        }
    }
}