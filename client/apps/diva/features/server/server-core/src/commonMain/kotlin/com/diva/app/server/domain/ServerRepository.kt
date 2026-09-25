package com.diva.app.server.domain

import com.diva.app.models.server.Server
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface ServerRepository : Repository {
    fun getServers(): Flow<Result<List<Server>>>

    fun getServer(id: String): Flow<Result<Option<Server>>>

    suspend fun getEnabledServers(): Result<List<Server>>

    suspend fun sync(): Result<Unit>

    suspend fun save(server: Server): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
