package com.diva.permission.api.client

import com.diva.models.api.ApiResponse
import com.diva.models.api.pagination.response.PaginationResponse
import com.diva.models.api.permission.dtos.UpdatePermissionDto
import com.diva.models.api.permission.dtos.UpdatePermissionRoleLevelDto
import com.diva.models.api.permission.response.PermissionResponse
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.ConstraintException
import io.github.juevigrace.diva.core.errors.HttpException
import io.github.juevigrace.diva.core.tryResult
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.toDivaNetworkException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

interface PermissionsApi {
    suspend fun list(
        page: Int,
        pageSize: Int,
        token: String
    ): Result<PaginationResponse<PermissionResponse>>
    suspend fun getByID(pid: String, token: String): Result<PermissionResponse>
    suspend fun update(pid: String, dto: UpdatePermissionDto, token: String): Result<Unit>
    suspend fun updateRoleLevel(pid: String, dto: UpdatePermissionRoleLevelDto, token: String): Result<PermissionResponse>
}

class PermissionsApiImpl(
    private val client: DivaClient
) : PermissionsApi {
    override suspend fun list(
        page: Int,
        pageSize: Int,
        token: String
    ): Result<PaginationResponse<PermissionResponse>> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/permissions",
                queryParams = mapOf(
                    "page" to page.toString(),
                    "limit" to pageSize.toString()
                ),
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<PaginationResponse<PermissionResponse>> = response.body()
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
                        url = Option.of("/api/permissions"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun getByID(pid: String, token: String): Result<PermissionResponse> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.get(
                path = "/api/permissions/$pid",
                headers = mapOf("Authorization" to "Bearer $token")
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<PermissionResponse> = response.body()
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
                        url = Option.of("/api/permissions/{pid}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun update(pid: String, dto: UpdatePermissionDto, token: String): Result<Unit> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.put(
                path = "/api/permissions/$pid",
                body = dto,
                headers = mapOf("Authorization" to "Bearer $token"),
                serializer = UpdatePermissionDto.serializer(),
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> return@tryResult
                else -> {
                    val body: ApiResponse<Nothing> = response.body()
                    throw HttpException(
                        statusCode = Option.of(response.status.value),
                        url = Option.of("/api/permissions/{pid}"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }

    override suspend fun updateRoleLevel(
        pid: String,
        dto: UpdatePermissionRoleLevelDto,
        token: String
    ): Result<PermissionResponse> {
        return tryResult(
            onError = { e -> e.toDivaNetworkException() }
        ) {
            val response: HttpResponse = client.patch(
                path = "/api/permissions/$pid/level",
                body = dto,
                headers = mapOf("Authorization" to "Bearer $token"),
                serializer = UpdatePermissionRoleLevelDto.serializer(),
            ).getOrThrow()
            when (response.status) {
                HttpStatusCode.OK -> {
                    val body: ApiResponse<PermissionResponse> = response.body()
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
                        url = Option.of("/api/permissions/{pid}/level"),
                        details = Option.of(body.message)
                    )
                }
            }
        }
    }
}
