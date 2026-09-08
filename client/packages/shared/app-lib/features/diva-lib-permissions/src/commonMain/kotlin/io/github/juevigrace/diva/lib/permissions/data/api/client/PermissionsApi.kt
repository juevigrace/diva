package io.github.juevigrace.diva.lib.permissions.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.pagination.PaginatedResponse
import io.github.juevigrace.diva.lib.models.api.permission.PermissionResponse
import io.github.juevigrace.diva.lib.models.api.permission.UpdatePermissionDto
import io.github.juevigrace.diva.lib.models.api.permission.UpdatePermissionRoleLevelDto
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.getAs
import io.github.juevigrace.diva.network.client.patchAs
import io.github.juevigrace.diva.network.client.put

interface PermissionsApi {
    suspend fun list(
        page: Int,
        pageSize: Int,
        token: String,
    ): Result<PaginatedResponse<PermissionResponse>>
    suspend fun getByID(pid: String, token: String): Result<PermissionResponse>
    suspend fun update(pid: String, dto: UpdatePermissionDto, token: String): Result<Unit>
    suspend fun updateRoleLevel(
        pid: String,
        dto: UpdatePermissionRoleLevelDto,
        token: String,
    ): Result<PermissionResponse>
}

class PermissionsApiImpl(
    private val client: DivaClient,
) : PermissionsApi {
    override suspend fun list(
        page: Int,
        pageSize: Int,
        token: String,
    ): Result<PaginatedResponse<PermissionResponse>> {
        return client.getAs<ApiResponse<PaginatedResponse<PermissionResponse>>>(
            path = "/api/permissions",
            queryParams = mapOf(
                "page" to page.toString(),
                "limit" to pageSize.toString(),
            ),
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }

    override suspend fun getByID(pid: String, token: String): Result<PermissionResponse> {
        return client.getAs<ApiResponse<PermissionResponse>>(
            path = "/api/permissions/$pid",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }

    override suspend fun update(pid: String, dto: UpdatePermissionDto, token: String): Result<Unit> {
        return client.put(
            path = "/api/permissions/$pid",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { }
    }

    override suspend fun updateRoleLevel(
        pid: String,
        dto: UpdatePermissionRoleLevelDto,
        token: String,
    ): Result<PermissionResponse> {
        return client.patchAs<ApiResponse<PermissionResponse>>(
            path = "/api/permissions/$pid/level",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }
}