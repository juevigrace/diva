package com.diva.app.database.folder

import kotlin.uuid.Uuid

interface MediaFolderLinkStorage {
    suspend fun link(mediaId: Uuid, folderId: Uuid): Result<Unit>

    suspend fun unlink(mediaId: Uuid, folderId: Uuid): Result<Unit>

    suspend fun removeAllByMedia(mediaId: Uuid): Result<Unit>

    suspend fun removeAllByFolder(folderId: Uuid): Result<Unit>
}
