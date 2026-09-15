package com.diva.app.database.folder

import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface FolderStorage {
    suspend fun getByUser(userId: Uuid): Result<List<Folder>>

    fun getByUserFlow(userId: Uuid): Flow<Result<List<Folder>>>

    suspend fun getById(id: Uuid): Result<Option<Folder>>

    fun getByIdFlow(id: Uuid): Flow<Result<Option<Folder>>>

    suspend fun getRoots(userId: Uuid): Result<List<Folder>>

    fun getRootsFlow(userId: Uuid): Flow<Result<List<Folder>>>

    suspend fun getChildren(userId: Uuid, parentId: Uuid): Result<List<Folder>>

    suspend fun getMediaByFolder(folderId: Uuid): Result<List<Media>>

    suspend fun upsert(item: Folder): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAllByUser(userId: Uuid): Result<Unit>
}
