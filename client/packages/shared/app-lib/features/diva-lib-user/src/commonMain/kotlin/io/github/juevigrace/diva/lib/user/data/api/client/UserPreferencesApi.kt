package io.github.juevigrace.diva.lib.user.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.user.preferences.CreateUserPreferencesDto
import io.github.juevigrace.diva.lib.models.api.user.preferences.UpdateUserPreferencesDto
import io.github.juevigrace.diva.lib.models.api.user.preferences.UserPreferencesResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.getAs
import io.github.juevigrace.diva.network.client.post
import io.github.juevigrace.diva.network.client.put

interface UserPreferencesApi {
    suspend fun getByUser(uid: String, token: String): Result<UserPreferencesResponse?>
    suspend fun create(uid: String, dto: CreateUserPreferencesDto, token: String): Result<Unit>
    suspend fun getByID(pid: String, token: String): Result<UserPreferencesResponse>
    suspend fun update(pid: String, dto: UpdateUserPreferencesDto, token: String): Result<Unit>
}

class UserPreferencesApiImpl(
    private val client: DivaClient,
) : UserPreferencesApi {
    override suspend fun getByUser(uid: String, token: String): Result<UserPreferencesResponse?> {
        return client.getAs<ApiResponse<UserPreferencesResponse?>>(
            path = "/api/user/$uid/preferences",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data }
    }

    override suspend fun create(uid: String, dto: CreateUserPreferencesDto, token: String): Result<Unit> {
        return client.post(
            path = "/api/user/$uid/preferences",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map {}
    }

    override suspend fun getByID(pid: String, token: String): Result<UserPreferencesResponse> {
        return client.getAs<ApiResponse<UserPreferencesResponse>>(
            path = "/api/user/preferences/$pid",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error("Missing data in ApiResponse") }
    }

    override suspend fun update(pid: String, dto: UpdateUserPreferencesDto, token: String): Result<Unit> {
        return client.put(
            path = "/api/user/preferences/$pid",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map {}
    }
}