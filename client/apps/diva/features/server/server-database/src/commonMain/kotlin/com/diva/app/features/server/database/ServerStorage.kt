package com.diva.app.features.server.database

import com.diva.app.database.server.DivaDB
import com.diva.app.models.server.Server
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow

interface ServerStorage {
    suspend fun getAll(): Result<List<Server>>

    fun getAllFlow(): Flow<Result<List<Server>>>

    suspend fun getById(id: String): Result<Option<Server>>

    fun getByIdFlow(id: String): Flow<Result<Option<Server>>>

    suspend fun getEnabled(): Result<List<Server>>

    suspend fun upsert(item: Server): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}

class ServerStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : ServerStorage {

    override suspend fun getAll(): Result<List<Server>> {
        return db.getList { serverQueries.findAll(::mapToServer) }
    }

    override fun getAllFlow(): Flow<Result<List<Server>>> {
        return db.getListAsFlow { serverQueries.findAll(::mapToServer) }
    }

    override suspend fun getById(id: String): Result<Option<Server>> {
        return db.getOne { serverQueries.findOneById(id, ::mapToServer) }
    }

    override fun getByIdFlow(id: String): Flow<Result<Option<Server>>> {
        return db.getOneAsFlow { serverQueries.findOneById(id, ::mapToServer) }
    }

    override suspend fun getEnabled(): Result<List<Server>> {
        return db.getList { serverQueries.findEnabled(::mapToServer) }
    }

    override suspend fun upsert(item: Server): Result<Unit> {
        return db.use {
            transaction {
                serverQueries.upsert(
                    id = item.id,
                    name = item.name,
                    base_url = item.baseUrl,
                    protocol = item.protocol,
                    port = item.port.map { it.toLong() }.getOrNull(),
                    enabled = item.enabled,
                    last_connected_at = item.lastConnectedAt.getOrNull(),
                    created_at = item.createdAt,
                    updated_at = item.updatedAt,
                )
            }
        }
    }

    override suspend fun delete(id: String): Result<Unit> {
        return db.use {
            transaction {
                serverQueries.deleteById(id)
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                serverQueries.findAll(::mapToServer)
                    .executeAsList()
                    .forEach { server ->
                        serverQueries.deleteById(server.id)
                    }
            }
        }
    }

    private fun mapToServer(
        id: String,
        name: String,
        baseUrl: String,
        protocol: String,
        port: Long?,
        enabled: Boolean,
        lastConnectedAt: Long?,
        createdAt: Long,
        updatedAt: Long,
    ): Server = Server(
        id = id,
        name = name,
        baseUrl = baseUrl,
        protocol = protocol,
        port = Option.of(port?.toInt()),
        enabled = enabled,
        lastConnectedAt = Option.of(lastConnectedAt),
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
