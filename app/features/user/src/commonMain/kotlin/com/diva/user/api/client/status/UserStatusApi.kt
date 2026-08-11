package com.diva.user.api.client.status

import com.diva.models.api.ApiResponse
import com.diva.models.api.user.state.dtos.UpdateUserStatusDto
import com.diva.models.api.user.state.dtos.UpdateVerifiedDto
import com.diva.models.api.user.state.response.UserStateResponse
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.HttpException
import io.github.juevigrace.diva.core.tryResult
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.toDivaNetworkException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

interface UserStatusApi {
    suspend fun getState(uid: String, token: String): Result<UserStateResponse?>
    suspend fun ping(uid: String, token: String): Result<Unit>
    suspend fun updateVerified(uid: String, dto: UpdateVerifiedDto, token: String): Result<Unit>
    suspend fun updateStatus(uid: String, dto: UpdateUserStatusDto, token: String): Result<Unit>
}

class UserStatusApiImpl(
    private val client: DivaClient
) : UserStatusApi {
    override suspend fun getState(uid: String, token: String): Result<UserStateResponse?> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/user/$uid/status",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<UserStateResponse?> = response.body()
                    body.data
                }
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/status"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun ping(uid: String, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.post(
                path = "/api/user/$uid/status/ping",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/status/ping"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun updateVerified(uid: String, dto: UpdateVerifiedDto, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.patch(
                path = "/api/user/$uid/status/verified",
                body = dto,
                headers = mapOf("Authorization" to "Bearer $token"),
                serializer = UpdateVerifiedDto.serializer(),
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.Accepted -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/status/verified"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun updateStatus(uid: String, dto: UpdateUserStatusDto, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.put(
                path = "/api/user/$uid/status",
                body = dto,
                headers = mapOf("Authorization" to "Bearer $token"),
                serializer = UpdateUserStatusDto.serializer(),
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.Accepted -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/status"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }
}
