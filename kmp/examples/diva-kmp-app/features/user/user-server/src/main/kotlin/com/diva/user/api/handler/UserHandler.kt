package com.diva.user.api.handler

import com.diva.mail.KMail
import com.diva.mail.buildCodeVerificationEmail
import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.auth.dtos.PasswordUpdateDto
import com.diva.models.api.auth.response.SessionResponse
import com.diva.models.api.user.dtos.CreateUserDto
import com.diva.models.api.user.dtos.EmailTokenDto
import com.diva.models.api.user.dtos.UpdateUserDto
import com.diva.models.api.user.dtos.UserEmailDto
import com.diva.models.api.user.preferences.dtos.UserPreferencesDto
import com.diva.models.api.user.response.UserResponse
import com.diva.models.auth.Session
import com.diva.user.data.UserPreferencesService
import com.diva.user.data.UserService
import com.diva.verification.data.VerificationService
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.ErrorCause
import io.github.juevigrace.diva.core.errors.toDivaError
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.core.network.HttpRequestMethod
import io.github.juevigrace.diva.core.network.HttpStatusCodes
import io.github.juevigrace.diva.core.onFailure
import io.github.juevigrace.diva.core.onSuccess
import io.github.juevigrace.diva.core.tryResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserHandler {

    suspend fun getUsers(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<UserResponse>>, DivaError>

    suspend fun getUser(id: String): DivaResult<ApiResponse<UserResponse>, DivaError>

    suspend fun createUser(dto: CreateUserDto, session: Session): DivaResult<ApiResponse<String>, DivaError>

    suspend fun updateUser(
        id: String,
        dto: UpdateUserDto,
        session: Session,
    ): DivaResult<ApiResponse<Unit>, DivaError>

    suspend fun updateEmail(
        dto: UserEmailDto,
        session: Session,
    ): DivaResult<ApiResponse<Unit>, DivaError>

    suspend fun deleteUser(id: String, session: Session): DivaResult<ApiResponse<Unit>, DivaError>

    suspend fun requestPasswordReset(dto: UserEmailDto): DivaResult<ApiResponse<Unit>, DivaError>

    suspend fun confirmPasswordReset(dto: EmailTokenDto): DivaResult<ApiResponse<SessionResponse>, DivaError>

    suspend fun resetPassword(dto: PasswordUpdateDto, session: Session): DivaResult<ApiResponse<Unit>, DivaError>

    suspend fun createPreferences(
        dto: UserPreferencesDto,
        session: Session,
    ): DivaResult<ApiResponse<Unit>, DivaError>

    suspend fun updatePreferences(
        dto: UserPreferencesDto,
        session: Session,
    ): DivaResult<ApiResponse<Unit>, DivaError>
}

class UserHandlerImpl(
    private val service: UserService,
    private val verificationService: VerificationService,
    private val preferencesService: UserPreferencesService,
    private val kMail: KMail,
) : UserHandler {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override suspend fun getUsers(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<UserResponse>>, DivaError> {
        return service.getUsers(page, pageSize)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getUser(id: String): DivaResult<ApiResponse<UserResponse>, DivaError> {
        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            val parsedId: Uuid = Uuid.parse(id)
            service.getUser(parsedId)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createUser(
        dto: CreateUserDto,
        session: Session
    ): DivaResult<ApiResponse<String>, DivaError> {
        if (session.user.permissions.isEmpty()) {
            // TODO: do an actual check of the permissions
            return DivaResult.failure(
                DivaError(
                    cause = ErrorCause.Network.Error(
                        method = HttpRequestMethod.POST,
                        url = "/api/user",
                        status = HttpStatusCodes.Forbidden,
                        details = Option.Some("You can't create users")
                    )
                )
            )
        }

        val userId: Uuid = service.createUser(dto).fold(
            onFailure = { err ->
                return DivaResult.failure(err)
            },
            onSuccess = { id -> id }
        )

        scope.launch {
            verificationService.createVerificationCode(userId)
                .onFailure { err -> println(err.message) }
                .onSuccess { v ->
                    kMail.sendEmail(
                        to = dto.email,
                        subject = "Email Verification",
                        html = buildCodeVerificationEmail(v)
                    )
                }
        }

        return DivaResult.success(ApiResponse(data = userId.toString(), message = "User created"))
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateUser(
        id: String,
        dto: UpdateUserDto,
        session: Session
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            val parsedId: Uuid = Uuid.parse(id)
            if (session.id != parsedId) {
                // TODO: permission check to forced update
                return@tryResult DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Network.Error(
                            method = HttpRequestMethod.PUT,
                            url = "/api/user/$id",
                            status = HttpStatusCodes.Forbidden,
                            details = Option.Some("You can't update users")
                        )
                    )
                )
            }
            service.updateUser(dto, parsedId)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateEmail(
        dto: UserEmailDto,
        session: Session
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        return service.updateEmail(dto, session.user.id)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deleteUser(
        id: String,
        session: Session
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            val parsedId: Uuid = Uuid.parse(id)
            if (session.id != parsedId) {
                // TODO: permission check to forced delete
                return@tryResult DivaResult.failure(
                    DivaError(
                        cause = ErrorCause.Network.Error(
                            method = HttpRequestMethod.DELETE,
                            url = "/api/user/$id",
                            status = HttpStatusCodes.Forbidden,
                            details = Option.Some("You can't delete users")
                        )
                    )
                )
            }
            service.deleteUser(parsedId)
        }
    }

    override suspend fun requestPasswordReset(dto: UserEmailDto): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun confirmPasswordReset(dto: EmailTokenDto): DivaResult<ApiResponse<SessionResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(
        dto: PasswordUpdateDto,
        session: Session
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createPreferences(
        dto: UserPreferencesDto,
        session: Session
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        return preferencesService.createPreferences(session.user.id, dto)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updatePreferences(
        dto: UserPreferencesDto,
        session: Session
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        // todo: i changed this somewhere
        return preferencesService.updatePreferences(dto)
    }
}
