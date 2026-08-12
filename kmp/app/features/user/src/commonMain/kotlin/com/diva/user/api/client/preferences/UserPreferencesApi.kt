package com.diva.user.api.client.preferences

import com.diva.models.api.ApiResponse
import com.diva.models.api.user.preferences.dtos.CreateUserPreferencesDto
import com.diva.models.api.user.preferences.dtos.UpdateUserPreferencesDto
import com.diva.models.api.user.preferences.responses.UserPreferencesResponse
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.ConstraintException
import io.github.juevigrace.diva.core.errors.HttpException
import io.github.juevigrace.diva.core.tryResult
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.toDivaNetworkException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

interface UserPreferencesApi {
    suspend fun getByUser(uid: String, token: String): Result<UserPreferencesResponse?>
    suspend fun getByID(pid: String, token: String): Result<UserPreferencesResponse>
    suspend fun create(
        uid: String,
        dto: CreateUserPreferencesDto,
        token: String
    ): Result<Unit>
    suspend fun update(
        pid: String,
        dto: UpdateUserPreferencesDto,
        token: String
    ): Result<Unit>
}

class UserPreferencesApiImpl(
    private val client: DivaClient
) : UserPreferencesApi {
    override suspend fun getByUser(uid: String, token: String): Result<UserPreferencesResponse?> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/user/$uid/preferences",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<UserPreferencesResponse?> = response.body()
                    body.data
                }
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/preferences"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun getByID(pid: String, token: String): Result<UserPreferencesResponse> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/user/preferences/$pid",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<UserPreferencesResponse> = response.body()
                    body.data ?: throw ConstraintException(
                        field = "data",
                        constraint = "missing",
                        value = body.message
                    )
                }
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/preferences/{pid}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun create(
        uid: String,
        dto: CreateUserPreferencesDto,
        token: String
    ): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.post(
                path = "/api/user/$uid/preferences",
                body = dto,
                headers = mapOf("Authorization" to "Bearer $token"),
                serializer = CreateUserPreferencesDto.serializer()
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.Created -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/preferences"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun update(
        pid: String,
        dto: UpdateUserPreferencesDto,
        token: String
    ): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.put(
                path = "/api/user/preferences/$pid",
                body = dto,
                headers = mapOf("Authorization" to "Bearer $token"),
                serializer = UpdateUserPreferencesDto.serializer()
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.Accepted -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/preferences/{pid}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }
}
