package io.github.juevigrace.diva.lib.user.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.auth.forgot.password.UpdatePasswordDto
import io.github.juevigrace.diva.lib.models.api.pagination.PaginatedResponse
import io.github.juevigrace.diva.lib.models.api.user.CreateUserDto
import io.github.juevigrace.diva.lib.models.api.user.UpdateEmailDto
import io.github.juevigrace.diva.lib.models.api.user.UpdatePhoneNumberDto
import io.github.juevigrace.diva.lib.models.api.user.UpdateRoleDto
import io.github.juevigrace.diva.lib.models.api.user.UpdateUsernameDto
import io.github.juevigrace.diva.lib.models.api.user.UserResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.deleteAs
import io.github.juevigrace.diva.network.client.getAs
import io.github.juevigrace.diva.network.client.patchAs
import io.github.juevigrace.diva.network.client.postAs

interface UserApi {
    suspend fun checkUsername(username: String): Result<Unit>
    suspend fun checkEmail(email: String): Result<Unit>
    suspend fun list(page: Int, pageSize: Int, token: String): Result<PaginatedResponse<UserResponse>>
    suspend fun create(dto: CreateUserDto, token: String): Result<Unit>
    suspend fun getByID(uid: String, token: String): Result<UserResponse>
    suspend fun updateEmail(uid: String, dto: UpdateEmailDto, token: String): Result<Unit>
    suspend fun updatePhone(uid: String, dto: UpdatePhoneNumberDto, token: String): Result<Unit>
    suspend fun updateUsername(uid: String, dto: UpdateUsernameDto, token: String): Result<Unit>
    suspend fun updatePassword(uid: String, dto: UpdatePasswordDto, token: String): Result<Unit>
    suspend fun updateRole(uid: String, dto: UpdateRoleDto, token: String): Result<Unit>
    suspend fun restore(uid: String, token: String): Result<Unit>
    suspend fun softDelete(uid: String, token: String): Result<Unit>
    suspend fun hardDelete(uid: String, token: String): Result<Unit>
}

class UserApiImpl(
    private val client: DivaClient,
) : UserApi {
    override suspend fun checkUsername(username: String): Result<Unit> {
        return client.getAs<Unit>(
            path = "/api/user/check/username/$username",
        )
    }

    override suspend fun checkEmail(email: String): Result<Unit> {
        return client.getAs<Unit>(
            path = "/api/user/check/email/$email",
        )
    }

    override suspend fun list(
        page: Int,
        pageSize: Int,
        token: String,
    ): Result<PaginatedResponse<UserResponse>> {
        return client.getAs<ApiResponse<PaginatedResponse<UserResponse>>>(
            path = "/api/user",
            queryParams = mapOf(
                "page" to page.toString(),
                "limit" to pageSize.toString(),
            ),
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }

    override suspend fun create(dto: CreateUserDto, token: String): Result<Unit> {
        return client.postAs<Unit>(
            path = "/api/user",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun getByID(uid: String, token: String): Result<UserResponse> {
        return client.getAs<ApiResponse<UserResponse>>(
            path = "/api/user/$uid",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }

    override suspend fun updateEmail(uid: String, dto: UpdateEmailDto, token: String): Result<Unit> {
        return client.patchAs<Unit>(
            path = "/api/user/$uid/email",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun updatePhone(uid: String, dto: UpdatePhoneNumberDto, token: String): Result<Unit> {
        return client.patchAs<Unit>(
            path = "/api/user/$uid/phone",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun updateUsername(uid: String, dto: UpdateUsernameDto, token: String): Result<Unit> {
        return client.patchAs<Unit>(
            path = "/api/user/$uid/username",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun updatePassword(uid: String, dto: UpdatePasswordDto, token: String): Result<Unit> {
        return client.patchAs<Unit>(
            path = "/api/user/$uid/password",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun updateRole(uid: String, dto: UpdateRoleDto, token: String): Result<Unit> {
        return client.patchAs<Unit>(
            path = "/api/user/$uid/role",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun restore(uid: String, token: String): Result<Unit> {
        return client.patchAs<Unit>(
            path = "/api/user/$uid/restore",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun softDelete(uid: String, token: String): Result<Unit> {
        return client.deleteAs<Unit>(
            path = "/api/user/$uid",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun hardDelete(uid: String, token: String): Result<Unit> {
        return client.deleteAs<Unit>(
            path = "/api/user/$uid/forever",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }
}
