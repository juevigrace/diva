package io.github.juevigrace.diva.lib.user.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.auth.session.SessionResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.deleteAs
import io.github.juevigrace.diva.network.client.getAs

interface UserSessionsApi {
    suspend fun list(uid: String, token: String): Result<List<SessionResponse>>
    suspend fun deleteAll(uid: String, token: String): Result<Unit>
    suspend fun closeByUser(uid: String, token: String): Result<Unit>
}

class UserSessionsApiImpl(
    private val client: DivaClient,
) : UserSessionsApi {
    override suspend fun list(uid: String, token: String): Result<List<SessionResponse>> {
        return client.getAs<ApiResponse<List<SessionResponse>>>(
            path = "/api/user/$uid/sessions",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: emptyList() }
    }

    override suspend fun deleteAll(uid: String, token: String): Result<Unit> {
        return client.deleteAs<Unit>(
            path = "/api/user/$uid/sessions",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun closeByUser(uid: String, token: String): Result<Unit> {
        return client.deleteAs<Unit>(
            path = "/api/user/$uid/sessions/close",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }
}
