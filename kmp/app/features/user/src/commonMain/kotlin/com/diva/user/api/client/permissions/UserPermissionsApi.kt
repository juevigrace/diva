package com.diva.user.api.client.permissions

import com.diva.models.api.ApiResponse
import com.diva.models.api.user.permissions.dtos.CreateUserPermissionDto
import com.diva.models.api.user.permissions.dtos.UpdateUserPermissionDto
import com.diva.models.api.user.permissions.response.UserPermissionResponse
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.ConstraintException
import io.github.juevigrace.diva.core.errors.HttpException
import io.github.juevigrace.diva.core.tryResult
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.toDivaNetworkException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

interface UserPermissionsApi {
    suspend fun getByUser(uid: String, token: String): Result<List<UserPermissionResponse>>
    suspend fun getOneByUser(uid: String, pid: String, token: String): Result<UserPermissionResponse>
    suspend fun create(
        uid: String,
        dto: CreateUserPermissionDto,
        token: String
    ): Result<Unit>
    suspend fun update(
        uid: String,
        pid: String,
        dto: UpdateUserPermissionDto,
        token: String
    ): Result<Unit>
    suspend fun delete(uid: String, pid: String, token: String): Result<Unit>
}

class UserPermissionsApiImpl(
    private val client: DivaClient
) : UserPermissionsApi {
    override suspend fun getByUser(uid: String, token: String): Result<List<UserPermissionResponse>> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/user/$uid/permissions",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<List<UserPermissionResponse>> = response.body()
                    body.data ?: emptyList()
                }
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/permissions"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun getOneByUser(uid: String, pid: String, token: String): Result<UserPermissionResponse> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/user/$uid/permissions/$pid",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<UserPermissionResponse> = response.body()
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
                        url = Option.of("/api/user/{uid}/permissions/{pid}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun create(
        uid: String,
        dto: CreateUserPermissionDto,
        token: String
    ): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.post(
                path = "/api/user/$uid/permissions",
                body = dto,
                headers = mapOf("Authorization" to "Bearer $token"),
                serializer = CreateUserPermissionDto.serializer(),
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.Created -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/permissions"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun update(
        uid: String,
        pid: String,
        dto: UpdateUserPermissionDto,
        token: String
    ): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.put(
                path = "/api/user/$uid/permissions/$pid",
                body = dto,
                headers = mapOf("Authorization" to "Bearer $token"),
                serializer = UpdateUserPermissionDto.serializer(),
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.Accepted -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/permissions/{pid}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun delete(uid: String, pid: String, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.delete(
                path = "/api/user/$uid/permissions/$pid",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.NoContent -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/user/{uid}/permissions/{pid}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }
}
