package com.diva.app.server.domain

import com.diva.app.server.models.Server
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface ServerRepository : Repository {
    fun observe(): Flow<Result<List<Server>>>

    fun observe(id: String): Flow<Result<Server>>

    suspend fun getEnabledServers(): Result<List<Server>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(server: Server): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
