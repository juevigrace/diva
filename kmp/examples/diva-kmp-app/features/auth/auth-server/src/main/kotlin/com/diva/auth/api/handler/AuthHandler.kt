package com.diva.auth.api.handler

import com.diva.auth.data.AuthService
import com.diva.mail.KMail
import com.diva.mail.buildCodeVerificationEmail
import com.diva.models.api.ApiResponse
import com.diva.models.api.auth.dtos.SessionDataDto
import com.diva.models.api.auth.dtos.SignInDto
import com.diva.models.api.auth.dtos.SignUpDto
import com.diva.models.api.auth.response.AuthResponse
import com.diva.models.api.auth.response.SessionResponse
import com.diva.models.auth.Session
import com.diva.models.user.User
import com.diva.user.data.UserService
import com.diva.verification.data.VerificationService
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.database.DatabaseAction
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.ErrorCause
import io.github.juevigrace.diva.core.errors.toDivaError
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.core.onFailure
import io.github.juevigrace.diva.core.onSuccess
import io.github.juevigrace.diva.core.tryResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface AuthHandler {
    suspend fun signIn(dto: SignInDto): DivaResult<ApiResponse<AuthResponse>, DivaError>
    suspend fun signUp(dto: SignUpDto): DivaResult<ApiResponse<AuthResponse>, DivaError>
    suspend fun signOut(session: Session): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun refresh(
        dto: SessionDataDto,
        session: Session
    ): DivaResult<ApiResponse<SessionResponse>, DivaError>
}

class AuthHandlerImpl(
    private val kMail: KMail,
    private val verificationService: VerificationService,
    private val userService: UserService,
    private val service: AuthService
) : AuthHandler {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun signIn(
        dto: SignInDto
    ): DivaResult<ApiResponse<AuthResponse>, DivaError> {
        val user: User = userService.getUserByUsername(dto.username)
            .fold(
                onFailure = { err ->
                    return DivaResult.failure(err)
                },
                onSuccess = { u -> u }
            )
        if (!user.userVerified) {
            verificationService.createVerificationCode(user.id)
                .onFailure { err ->
                    // TODO: duplicated keys are Exceptions and are not handled the way they should.
                    if (err.cause !is ErrorCause.Error.Ex) {
                        return DivaResult.failure(err)
                    }
                }
                .onSuccess { v ->
                    kMail.sendEmail(
                        to = user.email,
                        subject = "Email Verification",
                        html = buildCodeVerificationEmail(v)
                    ).onFailure { err ->
                        return DivaResult.failure(err)
                    }
                }
        }
        return service.signIn(dto, user)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun signUp(
        dto: SignUpDto
    ): DivaResult<ApiResponse<AuthResponse>, DivaError> {
        // TODO: try avoid checking for conflicts, handle them in the validation layer
        userService.getUserByUsername(dto.user.username)
            .map { _ ->
                return DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Database.Duplicated(
                            field = "username",
                            value = dto.user.username,
                            action = DatabaseAction.INSERT,
                            table = Option.Some("diva_user"),
                        )
                    )
                )
            }

        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            val userId: Uuid = userService.createUser(dto.user)
                .fold(
                    onFailure = { err ->
                        return@tryResult DivaResult.failure(err)
                    },
                    onSuccess = { id -> id }
                )

            scope.launch {
                verificationService.createVerificationCode(userId)
                    .onFailure { err ->
                        // TODO: Create proper logger
                        println(err.message)
                    }
                    .onSuccess { v ->
                        kMail.sendEmail(
                            to = dto.user.email,
                            subject = "Email Verification",
                            html = buildCodeVerificationEmail(v)
                        )
                    }
            }

            service.signUp(dto, userId)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun signOut(
        session: Session
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        return service.signOut(session)
    }

    override suspend fun refresh(
        dto: SessionDataDto,
        session: Session
    ): DivaResult<ApiResponse<SessionResponse>, DivaError> {
        return service.refresh(dto, session)
    }
}
