package com.diva.app.database.media

import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface MediaStorage {
    suspend fun getAll(): Result<List<Media>>

    fun getAllFlow(): Flow<Result<List<Media>>>

    suspend fun getById(id: Uuid): Result<Option<Media>>

    fun getByIdFlow(id: Uuid): Flow<Result<Option<Media>>>

    suspend fun upsert(item: Media): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
