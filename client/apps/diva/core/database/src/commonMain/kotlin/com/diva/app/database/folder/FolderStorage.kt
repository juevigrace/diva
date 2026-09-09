package com.diva.app.database.folder

import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface FolderStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getByUser(userId: Uuid): Result<List<Folder>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByUserFlow(userId: Uuid): Flow<Result<List<Folder>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(id: Uuid): Result<Option<Folder>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByIdFlow(id: Uuid): Flow<Result<Option<Folder>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getRoots(userId: Uuid): Result<List<Folder>>

    @OptIn(ExperimentalUuidApi::class)
    fun getRootsFlow(userId: Uuid): Flow<Result<List<Folder>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getChildren(userId: Uuid, parentId: Uuid): Result<List<Folder>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getMediaByFolder(folderId: Uuid): Result<List<Media>>

    suspend fun upsert(item: Folder): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun deleteAllByUser(userId: Uuid): Result<Unit>
}
