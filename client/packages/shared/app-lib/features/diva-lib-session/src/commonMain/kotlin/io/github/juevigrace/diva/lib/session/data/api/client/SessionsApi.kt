package io.github.juevigrace.diva.lib.session.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.auth.session.SessionResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.delete
import io.github.juevigrace.diva.network.client.getAs

interface SessionsApi {
    suspend fun listAll(token: String): Result<List<SessionResponse>>
    suspend fun getByID(sid: String, token: String): Result<SessionResponse>
    suspend fun close(sid: String, token: String): Result<Unit>
    suspend fun closeExpired(token: String): Result<Unit>
    suspend fun deleteAllForever(token: String): Result<Unit>
}

class SessionsApiImpl(
    private val client: DivaClient,
) : SessionsApi {
    override suspend fun listAll(token: String): Result<List<SessionResponse>> {
        return client.getAs<ApiResponse<List<SessionResponse>>>(
            path = "/api/sessions",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: emptyList() }
    }

    override suspend fun getByID(sid: String, token: String): Result<SessionResponse> {
        return client.getAs<ApiResponse<SessionResponse>>(
            path = "/api/sessions/$sid",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }

    override suspend fun close(sid: String, token: String): Result<Unit> {
        return client.delete(
            path = "/api/sessions/$sid/close",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { }
    }

    override suspend fun closeExpired(token: String): Result<Unit> {
        return client.delete(
            path = "/api/sessions/close",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { }
    }

    override suspend fun deleteAllForever(token: String): Result<Unit> {
        return client.delete(
            path = "/api/sessions",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { }
    }
}