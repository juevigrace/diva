package com.diva.app.folder.domain

import com.diva.app.folder.models.Folder
import com.diva.app.media.models.Media
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface FolderRepository : Repository {
    fun getFolders(userId: String): Flow<Result<List<Folder>>>

    fun getFolder(id: String): Flow<Result<Option<Folder>>>

    fun getRoots(userId: String): Flow<Result<List<Folder>>>

    suspend fun getChildren(userId: String, parentId: String): Result<List<Folder>>

    suspend fun getMediaByFolder(folderId: String): Result<List<Media>>

    suspend fun linkMedia(mediaId: String, folderId: String): Result<Unit>

    suspend fun unlinkMedia(mediaId: String, folderId: String): Result<Unit>

    suspend fun sync(): Result<Unit>

    suspend fun save(folder: Folder): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAllByUser(userId: String): Result<Unit>
}
