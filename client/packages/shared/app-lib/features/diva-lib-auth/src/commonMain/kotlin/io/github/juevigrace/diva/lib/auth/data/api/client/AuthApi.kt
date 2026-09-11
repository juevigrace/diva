package io.github.juevigrace.diva.lib.auth.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.auth.forgot.password.ForgotPasswordConfirmDto
import io.github.juevigrace.diva.lib.models.api.auth.session.SessionDataDto
import io.github.juevigrace.diva.lib.models.api.auth.session.SessionResponse
import io.github.juevigrace.diva.lib.models.api.auth.signin.SignInDto
import io.github.juevigrace.diva.lib.models.api.auth.signup.SignUpDto
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.postAs

interface AuthApi {
    suspend fun signIn(dto: SignInDto): Result<SessionResponse>
    suspend fun signUp(dto: SignUpDto): Result<SessionResponse>
    suspend fun signOut(dto: SessionDataDto, token: String): Result<Unit>
    suspend fun ping(token: String): Result<Unit>
    suspend fun refresh(dto: SessionDataDto, token: String): Result<SessionResponse>
    suspend fun forgotPasswordConfirm(dto: ForgotPasswordConfirmDto): Result<SessionResponse>
}

class AuthApiImpl(
    private val client: DivaClient,
) : AuthApi {
    override suspend fun signIn(dto: SignInDto): Result<SessionResponse> {
        return client.postAs<ApiResponse<SessionResponse>>(
            path = "/api/auth/signIn",
            body = dto,
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }

    override suspend fun signUp(dto: SignUpDto): Result<SessionResponse> {
        return client.postAs<ApiResponse<SessionResponse>>(
            path = "/api/auth/signUp",
            body = dto,
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }

    override suspend fun signOut(dto: SessionDataDto, token: String): Result<Unit> {
        return client.postAs<Unit>(
            path = "/api/auth/signOut",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun ping(token: String): Result<Unit> {
        return client.postAs<Unit>(
            path = "/api/auth/ping",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun refresh(dto: SessionDataDto, token: String): Result<SessionResponse> {
        return client.postAs<ApiResponse<SessionResponse>>(
            path = "/api/auth/refresh",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }

    override suspend fun forgotPasswordConfirm(dto: ForgotPasswordConfirmDto): Result<SessionResponse> {
        return client.postAs<ApiResponse<SessionResponse>>(
            path = "/api/auth/forgot/password/confirm",
            body = dto,
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }
}