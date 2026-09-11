package io.github.juevigrace.diva.lib.user.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.user.action.UserActionResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.delete
import io.github.juevigrace.diva.network.client.getAs

interface UserActionsApi {
    suspend fun list(uid: String, token: String): Result<List<UserActionResponse>>
    suspend fun getByID(aid: String, token: String): Result<UserActionResponse>
    suspend fun delete(aid: String, token: String): Result<Unit>
}

class UserActionsApiImpl(
    private val client: DivaClient,
) : UserActionsApi {
    override suspend fun list(uid: String, token: String): Result<List<UserActionResponse>> {
        return client.getAs<ApiResponse<List<UserActionResponse>>>(
            path = "/api/user/$uid/actions",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: emptyList() }
    }

    override suspend fun getByID(aid: String, token: String): Result<UserActionResponse> {
        return client.getAs<ApiResponse<UserActionResponse>>(
            path = "/api/user/actions/$aid",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }

    override suspend fun delete(aid: String, token: String): Result<Unit> {
        return client.delete(
            path = "/api/user/actions/$aid",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }
}