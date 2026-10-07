package com.diva.app.server.data

import com.diva.app.server.database.ServerStorage
import com.diva.app.server.domain.ServerRepository
import com.diva.app.server.models.Server
import io.github.juevigrace.diva.core.getOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ServerRepositoryImpl(
    private val storage: ServerStorage,
) : ServerRepository {

    override fun observe(): Flow<Result<List<Server>>> = storage.getAllFlow()

    override fun observe(id: String): Flow<Result<Server>> = storage.getByIdFlow(id).map { result ->
        result.mapCatching { option ->
            option.getOrThrow { IllegalStateException("No server '$id'") }
        }
    }

    override suspend fun getEnabledServers(): Result<List<Server>> = storage.getEnabled()

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun upsert(server: Server): Result<Unit> = storage.upsert(server)

    override suspend fun delete(id: String): Result<Unit> = storage.delete(id)

    override suspend fun deleteAll(): Result<Unit> = storage.deleteAll()
}
