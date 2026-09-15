package com.diva.app.database.server

import com.diva.app.models.server.Server
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface ServerStorage {
    suspend fun getAll(): Result<List<Server>>

    fun getAllFlow(): Flow<Result<List<Server>>>

    suspend fun getById(id: Uuid): Result<Option<Server>>

    fun getByIdFlow(id: Uuid): Flow<Result<Option<Server>>>

    suspend fun getEnabled(): Result<List<Server>>

    suspend fun upsert(item: Server): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
