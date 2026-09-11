package io.github.juevigrace.diva.lib.user.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.user.permissions.CreateUserPermissionDto
import io.github.juevigrace.diva.lib.models.api.user.permissions.UpdateUserPermissionDto
import io.github.juevigrace.diva.lib.models.api.user.permissions.UserPermissionResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.deleteAs
import io.github.juevigrace.diva.network.client.getAs
import io.github.juevigrace.diva.network.client.postAs
import io.github.juevigrace.diva.network.client.putAs

interface UserPermissionsApi {
    suspend fun list(uid: String, token: String): Result<List<UserPermissionResponse>>
    suspend fun getByID(uid: String, pid: String, token: String): Result<UserPermissionResponse>
    suspend fun create(uid: String, dto: CreateUserPermissionDto, token: String): Result<Unit>
    suspend fun update(uid: String, pid: String, dto: UpdateUserPermissionDto, token: String): Result<Unit>
    suspend fun delete(uid: String, pid: String, token: String): Result<Unit>
}

class UserPermissionsApiImpl(
    private val client: DivaClient,
) : UserPermissionsApi {
    override suspend fun list(uid: String, token: String): Result<List<UserPermissionResponse>> {
        return client.getAs<ApiResponse<List<UserPermissionResponse>>>(
            path = "/api/user/$uid/permissions",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: emptyList() }
    }

    override suspend fun getByID(uid: String, pid: String, token: String): Result<UserPermissionResponse> {
        return client.getAs<ApiResponse<UserPermissionResponse>>(
            path = "/api/user/$uid/permissions/$pid",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }

    override suspend fun create(uid: String, dto: CreateUserPermissionDto, token: String): Result<Unit> {
        return client.postAs<Unit>(
            path = "/api/user/$uid/permissions",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun update(
        uid: String,
        pid: String,
        dto: UpdateUserPermissionDto,
        token: String,
    ): Result<Unit> {
        return client.putAs<Unit>(
            path = "/api/user/$uid/permissions/$pid",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun delete(uid: String, pid: String, token: String): Result<Unit> {
        return client.deleteAs<Unit>(
            path = "/api/user/$uid/permissions/$pid",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }
}
