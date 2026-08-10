package com.diva.auth.data

import com.diva.database.session.SessionStorage
import com.diva.models.actions.AppActions
import com.diva.models.api.ApiResponse
import com.diva.models.api.action.ActionResponse
import com.diva.models.api.auth.dtos.SessionDataDto
import com.diva.models.api.auth.dtos.SignInDto
import com.diva.models.api.auth.dtos.SignUpDto
import com.diva.models.api.auth.response.AuthResponse
import com.diva.models.api.auth.response.SessionResponse
import com.diva.models.auth.Session
import com.diva.models.session.SessionStatus
import com.diva.models.user.User
import com.diva.util.Encryption
import com.diva.util.JwtHelper
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.ErrorCause
import io.github.juevigrace.diva.core.errors.toDivaError
import io.github.juevigrace.diva.core.getOrElse
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.core.mapError
import io.github.juevigrace.diva.core.network.HttpRequestMethod
import io.github.juevigrace.diva.core.network.HttpStatusCodes
import io.github.juevigrace.diva.core.onFailure
import io.github.juevigrace.diva.core.tryResult
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface AuthService {
    suspend fun refresh(
        dto: SessionDataDto,
        session: Session
    ): DivaResult<ApiResponse<SessionResponse>, DivaError>
    suspend fun signIn(
        dto: SignInDto,
        user: User,
    ): DivaResult<ApiResponse<AuthResponse>, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun signUp(
        dto: SignUpDto,
        userId: Uuid,
    ): DivaResult<ApiResponse<AuthResponse>, DivaError>

    suspend fun signOut(session: Session): DivaResult<ApiResponse<Unit>, DivaError>
}

class AuthServiceImpl(
    private val storage: SessionStorage,
    private val jwtHelper: JwtHelper,
) : AuthService {
    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    override suspend fun refresh(
        dto: SessionDataDto,
        session: Session
    ): DivaResult<ApiResponse<SessionResponse>, DivaError> {
        if (session.device != dto.device || session.userAgent != dto.userAgent) {
            storage.delete(session.id)
                .onFailure { err ->
                    // TODO: Create proper logger, and handle this type of cases
                    println(err)
                }
            return DivaResult.failure(
                DivaError(
                    cause = ErrorCause.Network.Error(
                        method = HttpRequestMethod.POST,
                        url = "/api/auth/refresh",
                        status = HttpStatusCodes.Unauthorized,
                        details = Option.Some("invalid session")
                    )
                )
            )
        }

        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            val updatedSession: Session = createSession(session.id, session.user.id, dto)
                .copy(
                    createdAt = session.createdAt,
                )

            storage.update(updatedSession).map {
                ApiResponse(
                    data = updatedSession.toResponse(),
                    message = "Session refreshed"
                )
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    override suspend fun signIn(
        dto: SignInDto,
        user: User,
    ): DivaResult<ApiResponse<AuthResponse>, DivaError> {
        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            when {
                !Encryption.verifyPassword(dto.password, user.passwordHash.getOrElse { "" }) -> {
                    DivaResult.failure(
                        DivaError(
                            cause = ErrorCause.Network.Error(
                                method = HttpRequestMethod.POST,
                                url = "/api/auth/signIn",
                                status = HttpStatusCodes.Unauthorized,
                                details = Option.Some("invalid credentials")
                            ),
                        )
                    )
                }
                else -> {
                    val sessionId: Uuid = Uuid.random()
                    val session: Session = createSession(sessionId, user.id, dto.sessionData)
                    storage.insert(session)
                        .map {
                            ApiResponse(
                                data = AuthResponse(
                                    session = session.toResponse(),
                                    actions = if (!user.userVerified) {
                                        listOf(
                                            ActionResponse(
                                                actionName = AppActions.EmailVerification.key,
                                            )
                                        )
                                    } else {
                                        emptyList()
                                    }
                                ),
                                message = "Sign in successful"
                            )
                        }
                }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    override suspend fun signUp(
        dto: SignUpDto,
        userId: Uuid
    ): DivaResult<ApiResponse<AuthResponse>, DivaError> {
        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            val sessionId: Uuid = Uuid.random()
            val session: Session = createSession(sessionId, userId, dto.sessionData)
            storage.insert(session).map {
                ApiResponse(
                    data = AuthResponse(
                        session = session.toResponse(),
                        actions = listOf(
                            ActionResponse(
                                actionName = AppActions.EmailVerification.key,
                            )
                        )
                    ),
                    message = "Sign up successful"
                )
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun signOut(session: Session): DivaResult<ApiResponse<Unit>, DivaError> {
        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            storage
                .update(session.copy(status = SessionStatus.CLOSED))
                .mapError { err -> err }
                .map { ApiResponse(data = Unit, message = "Sign out successful") }
        }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    private fun createSession(sessionId: Uuid, userId: Uuid, dto: SessionDataDto): Session {
        val accessToken: String = jwtHelper.createAccessToken(
            userId = userId.toString(),
            sessionId = sessionId.toString()
        )
        val refreshToken: String = jwtHelper.createRefreshToken(
            userId = userId.toString(),
            sessionId = sessionId.toString()
        )

        val now: Instant = Clock.System.now()
        val newSession = Session(
            id = sessionId,
            user = User(id = userId),
            accessToken = accessToken,
            refreshToken = refreshToken,
            device = dto.device,
            status = SessionStatus.ACTIVE,
            // TODO: GET IP
            ipAddress = dto.ipAddress ?: "",
            userAgent = dto.userAgent ?: "",
            expiresAt = now.plus(Duration.parse(SESSION_DURATION_MS)),
            createdAt = now,
            updatedAt = now
        )
        return newSession
    }
    companion object {
        private const val SESSION_DURATION_MS = "86400000ms"
    }
}
