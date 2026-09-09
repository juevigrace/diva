package com.diva.app.features.server.domain

import com.diva.app.models.server.Server
import io.github.juevigrace.diva.core.*
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface ServerRepository : Repository {
    fun getServers(): Flow<Result<List<Server>>>

    fun getServer(id: Uuid): Flow<Result<Option<Server>>>

    suspend fun getEnabledServers(): Result<List<Server>>

    suspend fun sync(): Result<Unit>

    suspend fun save(server: Server): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}