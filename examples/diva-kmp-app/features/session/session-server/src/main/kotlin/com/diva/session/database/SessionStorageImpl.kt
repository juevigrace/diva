package com.diva.session.database

import com.diva.database.DivaDB
import com.diva.database.session.SessionStorage
import com.diva.models.auth.Session
import com.diva.models.roles.Role
import com.diva.models.session.SessionStatus
import com.diva.models.user.User
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.database.DatabaseAction
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.ErrorCause
import io.github.juevigrace.diva.database.DivaDatabase
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

class SessionStorageImpl(
    private val db: DivaDatabase<DivaDB>
) : SessionStorage {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getById(id: Uuid): DivaResult<Option<Session>, DivaError> {
        return db.getOne { sessionQueries.findOneById(id.toJavaUuid(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun insert(item: Session): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                val expiresAt: OffsetDateTime = OffsetDateTime.ofInstant(
                    item.expiresAt.toJavaInstant(),
                    ZoneOffset.UTC
                )
                sessionQueries.insert(
                    id = item.id.toJavaUuid(),
                    user_id = item.user.id.toJavaUuid(),
                    access_token = item.accessToken,
                    refresh_token = item.refreshToken,
                    device = item.device,
                    status = item.status,
                    ip_address = item.ipAddress,
                    user_agent = item.userAgent,
                    expires_at = expiresAt,
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.INSERT,
                            table = Option.Some("diva_session"),
                            details = Option.Some("Failed to insert")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun update(item: Session): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                val expiresAt: OffsetDateTime = OffsetDateTime.ofInstant(
                    item.expiresAt.toJavaInstant(),
                    ZoneOffset.UTC
                )
                sessionQueries.update(
                    access_token = item.accessToken,
                    refresh_token = item.refreshToken,
                    device = item.device,
                    status = item.status,
                    ip_address = item.ipAddress,
                    user_agent = item.userAgent,
                    expires_at = expiresAt,
                    id = item.id.toJavaUuid()
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_session"),
                            details = Option.Some("Failed to update")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun delete(id: Uuid): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                sessionQueries.delete(id.toJavaUuid()).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.DELETE,
                            table = Option.Some("diva_session"),
                            details = Option.Some("Failed to delete")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deleteSessionsByUser(userId: Uuid): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                sessionQueries.deleteByUserId(userId.toJavaUuid()).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.DELETE,
                            table = Option.Some("diva_session"),
                            details = Option.Some("Failed to delete")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    private fun mapToEntity(
        id: UUID,
        accessToken: String,
        refreshToken: String,
        device: String,
        status: SessionStatus,
        ipAddress: String,
        userAgent: String,
        expiresAt: OffsetDateTime,
        createdAt: OffsetDateTime,
        updatedAt: OffsetDateTime,
        userId: UUID?,
        email: String?,
        username: String?,
        userVerified: Boolean?,
        role: Role?,
        uCreatedAt: OffsetDateTime?,
        uUpdatedAt: OffsetDateTime?,
    ): Session {
        require(userId != null) { "User ID cannot be null" }
        require(email != null) { "Email cannot be null" }
        require(username != null) { "Username cannot be null" }
        require(uCreatedAt != null) { "User created at cannot be null" }
        require(uUpdatedAt != null) { "User updated at cannot be null" }

        return Session(
            id = id.toKotlinUuid(),
            user = User(
                id = userId.toKotlinUuid(),
                username = username,
                email = email,
                userVerified = userVerified ?: false,
                role = role ?: Role.USER,
                createdAt = uCreatedAt.toInstant().toKotlinInstant(),
                updatedAt = uUpdatedAt.toInstant().toKotlinInstant()
            ),
            accessToken = accessToken,
            refreshToken = refreshToken,
            device = device,
            status = status,
            ipAddress = ipAddress,
            userAgent = userAgent,
            expiresAt = expiresAt.toInstant().toKotlinInstant(),
            expired = expiresAt.toInstant().toKotlinInstant() < Clock.System.now(),
            createdAt = createdAt.toInstant().toKotlinInstant(),
            updatedAt = updatedAt.toInstant().toKotlinInstant(),
        )
    }
}
