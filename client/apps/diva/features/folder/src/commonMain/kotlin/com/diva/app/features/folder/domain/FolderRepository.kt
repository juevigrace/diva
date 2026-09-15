package com.diva.app.features.folder.domain

import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface FolderRepository : Repository {
    fun getFolders(userId: Uuid): Flow<Result<List<Folder>>>

    fun getFolder(id: Uuid): Flow<Result<Option<Folder>>>

    fun getRoots(userId: Uuid): Flow<Result<List<Folder>>>

    suspend fun getChildren(userId: Uuid, parentId: Uuid): Result<List<Folder>>

    suspend fun getMediaByFolder(folderId: Uuid): Result<List<Media>>

    suspend fun linkMedia(mediaId: Uuid, folderId: Uuid): Result<Unit>

    suspend fun unlinkMedia(mediaId: Uuid, folderId: Uuid): Result<Unit>

    suspend fun sync(): Result<Unit>

    suspend fun save(folder: Folder): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAllByUser(userId: Uuid): Result<Unit>
}