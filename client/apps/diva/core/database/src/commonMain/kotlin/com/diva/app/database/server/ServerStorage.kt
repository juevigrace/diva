package com.diva.app.database.server

import com.diva.app.models.server.Server
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ServerStorage {
    suspend fun getAll(): Result<List<Server>>

    fun getAllFlow(): Flow<Result<List<Server>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(id: Uuid): Result<Option<Server>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByIdFlow(id: Uuid): Flow<Result<Option<Server>>>

    suspend fun getEnabled(): Result<List<Server>>

    suspend fun upsert(item: Server): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
