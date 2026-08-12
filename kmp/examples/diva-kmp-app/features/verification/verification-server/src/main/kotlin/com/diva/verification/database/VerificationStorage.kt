package com.diva.verification.database

import com.diva.database.DivaDB
import com.diva.models.server.UserVerification
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.database.DatabaseAction
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.ErrorCause
import io.github.juevigrace.diva.database.DivaDatabase
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

interface VerificationStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(id: Uuid): DivaResult<Option<UserVerification>, DivaError>

    suspend fun insert(item: UserVerification): DivaResult<Unit, DivaError>

    suspend fun update(item: UserVerification): DivaResult<Unit, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): DivaResult<Unit, DivaError>

    suspend fun getByToken(token: String): DivaResult<Option<UserVerification>, DivaError>
    suspend fun deleteByToken(token: String): DivaResult<Unit, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun verifyUser(userId: Uuid): DivaResult<Unit, DivaError>
}

class VerificationStorageImpl(
    private val db: DivaDatabase<DivaDB>
) : VerificationStorage {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getById(id: Uuid): DivaResult<Option<UserVerification>, DivaError> {
        return db.getOne { emailVerificationTokensQueries.findOneByUserId(id.toJavaUuid(), mapper = ::mapToEntity) }
    }

    override suspend fun getByToken(token: String): DivaResult<Option<UserVerification>, DivaError> {
        return db.getOne { emailVerificationTokensQueries.findOneByToken(token, mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    override suspend fun insert(item: UserVerification): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                val expiresAt: OffsetDateTime = OffsetDateTime.ofInstant(
                    item.expiresAt.toJavaInstant(),
                    ZoneOffset.UTC
                )

                emailVerificationTokensQueries.insert(
                    user_id = item.userId.toJavaUuid(),
                    token = item.token,
                    expires_at = expiresAt,
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.INSERT,
                            table = Option.Some("diva_email_verification_tokens"),
                            details = Option.Some("Failed to insert")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    override suspend fun update(item: UserVerification): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                emailVerificationTokensQueries.update(item.userId.toJavaUuid()).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_email_verification_tokens"),
                            details = Option.Some("Failed to update")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun verifyUser(userId: Uuid): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                userQueries.updateVerified(true, userId.toJavaUuid()).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_user"),
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
                emailVerificationTokensQueries.delete(id.toJavaUuid()).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.DELETE,
                            table = Option.Some("diva_email_verification_tokens"),
                            details = Option.Some("Failed to delete")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    override suspend fun deleteByToken(token: String): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                emailVerificationTokensQueries.deleteByToken(token).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.DELETE,
                            table = Option.Some("diva_email_verification_tokens"),
                            details = Option.Some("Failed to delete")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    private fun mapToEntity(
        userId: UUID,
        token: String,
        expiresAt: OffsetDateTime,
        createdAt: OffsetDateTime,
        usedAt: OffsetDateTime?
    ): UserVerification {
        return UserVerification(
            userId = userId.toKotlinUuid(),
            token = token,
            expiresAt = expiresAt.toInstant().toKotlinInstant(),
            createdAt = createdAt.toInstant().toKotlinInstant(),
            usedAt = Option.of(usedAt?.toInstant()?.toKotlinInstant())
        )
    }
}
