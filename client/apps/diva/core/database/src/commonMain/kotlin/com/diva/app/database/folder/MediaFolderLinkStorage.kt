package com.diva.app.database.folder

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface MediaFolderLinkStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun link(mediaId: Uuid, folderId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun unlink(mediaId: Uuid, folderId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun removeAllByMedia(mediaId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun removeAllByFolder(folderId: Uuid): Result<Unit>
}
