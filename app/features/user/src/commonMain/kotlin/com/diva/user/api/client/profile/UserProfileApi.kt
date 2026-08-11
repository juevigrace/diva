package com.diva.user.api.client.profile

import com.diva.models.api.ApiResponse
import com.diva.models.api.user.profile.dtos.CreateProfileDto
import com.diva.models.api.user.profile.dtos.UpdateProfileDto
import com.diva.models.api.user.profile.response.UserProfileResponse
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.HttpException
import io.github.juevigrace.diva.core.tryResult
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.toDivaNetworkException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

interface UserProfileApi {
    suspend fun getByUser(uid: String, token: String): Result<UserProfileResponse?>
    suspend fun create(uid: String, dto: CreateProfileDto, token: String): Result<Unit>
    suspend fun update(uid: String, dto: UpdateProfileDto, token: String): Result<Unit>
}

class UserProfileApiImpl(
    private val client: DivaClient
) : UserProfileApi {
    override suspend fun getByUser(uid: String, token: String): Result<UserProfileResponse?> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/user/$uid/profile",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<UserProfileResponse?> = response.body()
                    body.data
                }
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/profile"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun create(uid: String, dto: CreateProfileDto, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.post(
                path = "/api/user/$uid/profile",
                body = dto,
                headers = mapOf("Authorization" to "Bearer $token"),
                serializer = CreateProfileDto.serializer(),
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.Created -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/profile"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun update(uid: String, dto: UpdateProfileDto, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.put(
                path = "/api/user/$uid/profile",
                body = dto,
                headers = mapOf("Authorization" to "Bearer $token"),
                serializer = UpdateProfileDto.serializer(),
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.Accepted -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/profile"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }
}
