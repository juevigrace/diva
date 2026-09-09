package com.diva.app.database.media

import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface MediaStorage {
    suspend fun getAll(): Result<List<Media>>

    fun getAllFlow(): Flow<Result<List<Media>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(id: Uuid): Result<Option<Media>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByIdFlow(id: Uuid): Flow<Result<Option<Media>>>

    suspend fun upsert(item: Media): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
