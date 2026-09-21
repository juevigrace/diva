package com.diva.app.features.server.data

import com.diva.app.features.server.database.ServerStorage
import com.diva.app.features.server.domain.ServerRepository
import com.diva.app.models.server.Server
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow

class ServerRepositoryImpl(
    private val storage: ServerStorage,
) : ServerRepository {

    override fun getServers(): Flow<Result<List<Server>>> = storage.getAllFlow()

    override fun getServer(id: String): Flow<Result<Option<Server>>> = storage.getByIdFlow(id)

    override suspend fun getEnabledServers(): Result<List<Server>> = storage.getEnabled()

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(server: Server): Result<Unit> = storage.upsert(server)

    override suspend fun delete(id: String): Result<Unit> = storage.delete(id)

    override suspend fun deleteAll(): Result<Unit> = storage.deleteAll()
}
