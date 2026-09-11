package io.github.juevigrace.diva.lib.user.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.user.state.UpdateUserStatusDto
import io.github.juevigrace.diva.lib.models.api.user.state.UpdateVerifiedDto
import io.github.juevigrace.diva.lib.models.api.user.state.UserStateResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.getAs
import io.github.juevigrace.diva.network.client.patch
import io.github.juevigrace.diva.network.client.post
import io.github.juevigrace.diva.network.client.put

interface UserStateApi {
    suspend fun getState(uid: String, token: String): Result<UserStateResponse?>
    suspend fun ping(uid: String, token: String): Result<Unit>
    suspend fun updateVerified(uid: String, dto: UpdateVerifiedDto, token: String): Result<Unit>
    suspend fun updateStatus(uid: String, dto: UpdateUserStatusDto, token: String): Result<Unit>
}

class UserStateApiImpl(
    private val client: DivaClient,
) : UserStateApi {
    override suspend fun getState(uid: String, token: String): Result<UserStateResponse?> {
        return client.getAs<ApiResponse<UserStateResponse?>>(
            path = "/api/user/$uid/status",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data }
    }

    override suspend fun ping(uid: String, token: String): Result<Unit> {
        return client.post(
            path = "/api/user/$uid/status/ping",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map {}
    }

    override suspend fun updateVerified(uid: String, dto: UpdateVerifiedDto, token: String): Result<Unit> {
        return client.patch(
            path = "/api/user/$uid/status/verified",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map {}
    }

    override suspend fun updateStatus(uid: String, dto: UpdateUserStatusDto, token: String): Result<Unit> {
        return client.put(
            path = "/api/user/$uid/status",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map {}
    }
}
