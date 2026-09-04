package io.github.juevigrace.diva.lib.session.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.DivaDB
import io.github.juevigrace.diva.lib.database.session.SessionStorage
import io.github.juevigrace.diva.lib.models.session.Session
import io.github.juevigrace.diva.lib.models.session.SessionData
import io.github.juevigrace.diva.lib.models.session.SessionStatus
import io.github.juevigrace.diva.lib.models.session.SessionType
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SessionStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : SessionStorage {

    override suspend fun getAll(): Result<List<Session>> {
        return db.getList {
            sessionQueries.findAll(::mapToSession)
        }
    }

    override fun getAllFlow(): Flow<Result<List<Session>>> {
        return db.getListAsFlow {
            sessionQueries.findAll(::mapToSession)
        }
    }

    override suspend fun getById(id: Uuid): Result<Option<Session>> {
        return db.getOne {
            sessionQueries.findOneById(id.toString(), ::mapToSession)
        }
    }

    override suspend fun getCurrent(): Result<Option<Session>> {
        return db.getOne {
            sessionQueries.findCurrent(::mapToSession)
        }
    }

    override fun getCurrentFlow(): Flow<Result<Option<Session>>> {
        return db.getOneAsFlow {
            sessionQueries.findCurrent(::mapToSession)
        }
    }

    override suspend fun upsert(item: Session): Result<Unit> {
        return db.use {
            transaction {
                sessionQueries.upsert(
                    id = item.id.toString(),
                    user_id = item.user.id.toString(),
                    access_token = item.accessToken,
                    refresh_token = item.refreshToken,
                    device_id = item.data.device,
                    is_current = item.isCurrent,
                    type = item.type,
                    status = item.status,
                    ip_address = item.data.ip,
                    user_agent = item.data.agent,
                    access_expires_at = item.accessExpiresAt.epochSeconds,
                    refresh_expires_at = item.refreshExpiresAt.epochSeconds,
                    created_at = item.createdAt.epochSeconds,
                    updated_at = item.updatedAt.epochSeconds
                )
            }
        }
    }

    override suspend fun delete(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                sessionQueries.deleteById(id.toString())
            }
        }
    }

    override suspend fun markCurrent(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                sessionQueries.updateCurrent(id.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                sessionQueries.deleteAll()
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
            id = Uuid.parse(id),
            user = User(id = Uuid.parse(userId)),
            accessToken = accessToken,
            refreshToken = refreshToken,
            type = type,
            status = status,
            isCurrent = isCurrent,
            data = SessionData(device = deviceId, agent = userAgent, ip = ipAddress),
            accessExpiresAt = Instant.fromEpochSeconds(accessExpiresAt),
            refreshExpiresAt = Instant.fromEpochSeconds(refreshExpiresAt),
            createdAt = Instant.fromEpochSeconds(createdAt),
            updatedAt = Instant.fromEpochSeconds(updatedAt)
        )
    }
}
