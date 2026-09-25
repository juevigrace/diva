package io.github.juevigrace.diva.lib.session.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.session.DivaSharedDB
import io.github.juevigrace.diva.lib.models.session.Session
import io.github.juevigrace.diva.lib.models.session.SessionData
import io.github.juevigrace.diva.lib.models.session.SessionStatus
import io.github.juevigrace.diva.lib.models.session.SessionType
import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    suspend fun findAll(): Result<List<Session>>

    fun findAllFlow(): Flow<Result<List<Session>>>

    suspend fun findOne(id: String): Result<Option<Session>>

    fun findOneFlow(id: String): Flow<Result<Option<Session>>>

    suspend fun findCurrent(): Result<Option<Session>>

    fun findCurrentFlow(): Flow<Result<Option<Session>>>

    suspend fun upsert(item: Session): Result<Unit>

    suspend fun deleteOne(id: String): Result<Unit>

    suspend fun markCurrent(id: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}

class SessionStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>,
) : SessionStorage {

    override suspend fun findAll(): Result<List<Session>> {
        return db.getList {
            sessionQueries.findAll(::mapToSession)
        }
    }

    override fun findAllFlow(): Flow<Result<List<Session>>> {
        return db.getListAsFlow {
            sessionQueries.findAll(::mapToSession)
        }
    }

    override suspend fun findOne(id: String): Result<Option<Session>> {
        return db.getOne {
            sessionQueries.findOne(id, ::mapToSession)
        }
    }

    override fun findOneFlow(id: String): Flow<Result<Option<Session>>> {
        return db.getOneAsFlow {
            sessionQueries.findOne(id, ::mapToSession)
        }
    }

    override suspend fun findCurrent(): Result<Option<Session>> {
        return db.getOne {
            sessionQueries.findCurrent(::mapToSession)
        }
    }

    override fun findCurrentFlow(): Flow<Result<Option<Session>>> {
        return db.getOneAsFlow {
            sessionQueries.findCurrent(::mapToSession)
        }
    }

    override suspend fun upsert(item: Session): Result<Unit> {
        return db.use {
            transaction {
                sessionQueries.upsert(
                    id = item.id,
                    user_id = item.userId,
                    access_token = item.accessToken,
                    refresh_token = item.refreshToken,
                    device_id = item.data.device,
                    type = item.type,
                    status = item.status,
                    ip_address = item.data.ip,
                    user_agent = item.data.agent,
                    access_expires_at = item.accessExpiresAt,
                    refresh_expires_at = item.refreshExpiresAt,
                    created_at = item.createdAt,
                    updated_at = item.updatedAt
                )
            }
        }
    }

    override suspend fun deleteOne(id: String): Result<Unit> {
        return db.use {
            transaction {
                sessionQueries.deleteOne(id)
            }
        }
    }

    override suspend fun markCurrent(id: String): Result<Unit> {
        return db.use {
            transaction {
                sessionQueries.updateCurrent(id)
            }
        }
    }

    override suspend fun delete(): Result<Unit> {
        return db.use {
            transaction {
                sessionQueries.delete()
            }
        }
    }

    @Suppress("LongParameterList")
    private fun mapToSession(
        id: String,
        userId: String,
        accessToken: String,
        refreshToken: String,
        deviceId: String,
        isCurrent: Boolean,
        type: SessionType,
        status: SessionStatus,
        ipAddress: String,
        userAgent: String,
        accessExpiresAt: Long,
        refreshExpiresAt: Long,
        createdAt: Long,
        updatedAt: Long,
    ): Session {
        return Session(
            id = id,
            userId = userId,
            accessToken = accessToken,
            refreshToken = refreshToken,
            type = type,
            status = status,
            isCurrent = isCurrent,
            data = SessionData(device = deviceId, agent = userAgent, ip = ipAddress),
            accessExpiresAt = accessExpiresAt,
            refreshExpiresAt = refreshExpiresAt,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
