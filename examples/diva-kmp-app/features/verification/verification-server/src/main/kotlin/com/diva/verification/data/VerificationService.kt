package com.diva.verification.data

import com.diva.models.server.UserVerification
import com.diva.verification.database.VerificationStorage
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.ErrorCause
import io.github.juevigrace.diva.core.errors.toDivaError
import io.github.juevigrace.diva.core.flatMap
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.core.isPresent
import io.github.juevigrace.diva.core.tryResult
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface VerificationService {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun createVerificationCode(userId: Uuid): DivaResult<UserVerification, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun verify(token: String): DivaResult<Unit, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun verifyUser(token: String, userId: Uuid): DivaResult<Unit, DivaError>

    suspend fun deleteToken(token: String): DivaResult<Unit, DivaError>
}

class VerificationServiceImpl(
    private val storage: VerificationStorage,
) : VerificationService {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createVerificationCode(userId: Uuid): DivaResult<UserVerification, DivaError> {
        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            val item = UserVerification(
                userId = userId,
                token = generateToken(),
                expiresAt = Clock.System.now().toLocalDateTime(TimeZone.UTC).toInstant(TimeZone.UTC) + 15.minutes,
                createdAt = Clock.System.now(),
            )

            storage.insert(item)
                .flatMap { DivaResult.success(item) }
        }
    }

    override suspend fun verify(token: String): DivaResult<Unit, DivaError> {
        return storage
            .getByToken(token)
            .flatMap { option ->
                option.fold(
                    onNone = {
                        DivaResult.failure(
                            DivaError(
                                cause = ErrorCause.Validation.MissingValue(
                                    "token",
                                    Option.Some("token doesn't exists")
                                )
                            )
                        )
                    },
                    onSome = { verification ->
                        when {
                            verification.expiresAt < Clock.System.now() -> {
                                DivaResult.failure(
                                    DivaError(
                                        cause = ErrorCause.Validation.Expired("token", Option.Some("token has expired"))
                                    )
                                )
                            }
                            verification.usedAt.isPresent() -> {
                                DivaResult.failure(
                                    DivaError(
                                        cause = ErrorCause.Validation.Used(
                                            "token",
                                            Option.Some("token has already been used")
                                        )
                                    )
                                )
                            }
                            else -> {
                                storage.update(verification)
                            }
                        }
                    }
                )
            }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun verifyUser(token: String, userId: Uuid): DivaResult<Unit, DivaError> {
        return verify(token).flatMap { storage.verifyUser(userId) }
    }

    override suspend fun deleteToken(token: String): DivaResult<Unit, DivaError> {
        return storage.deleteByToken(token)
    }

    private fun generateToken(): String {
        return Random.nextInt(LOWER_BOUND, UPPER_BOUND).toString()
    }

    companion object {
        private const val LOWER_BOUND = 100000
        private const val UPPER_BOUND = 999999
    }
}
