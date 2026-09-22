package io.github.juevigrace.diva.lib.user.data.api.client

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.user.preferences.CreateUserPreferencesDto
import io.github.juevigrace.diva.lib.models.api.user.preferences.UpdateUserPreferencesDto
import io.github.juevigrace.diva.lib.models.api.user.preferences.UserPreferencesResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.getAs
import io.github.juevigrace.diva.network.client.postAs
import io.github.juevigrace.diva.network.client.putAs

interface UserPreferencesApi {
    suspend fun getByUser(uid: String, token: String): Result<Option<UserPreferencesResponse>>
    suspend fun create(uid: String, dto: CreateUserPreferencesDto, token: String): Result<Unit>
    suspend fun getByID(pid: String, token: String): Result<UserPreferencesResponse>
    suspend fun update(pid: String, dto: UpdateUserPreferencesDto, token: String): Result<Unit>
}

class UserPreferencesApiImpl(
    private val client: DivaClient,
) : UserPreferencesApi {
    override suspend fun getByUser(uid: String, token: String): Result<Option<UserPreferencesResponse>> {
        return client.getAs<ApiResponse<UserPreferencesResponse?>>(
            path = "/api/user/$uid/preferences",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data.toOption() }
    }

    override suspend fun create(uid: String, dto: CreateUserPreferencesDto, token: String): Result<Unit> {
        return client.postAs<Unit>(
            path = "/api/user/$uid/preferences",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun getByID(pid: String, token: String): Result<UserPreferencesResponse> {
        return client.getAs<ApiResponse<UserPreferencesResponse>>(
            path = "/api/user/preferences/$pid",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error(it.message) }
    }

    override suspend fun update(pid: String, dto: UpdateUserPreferencesDto, token: String): Result<Unit> {
        return client.putAs<Unit>(
            path = "/api/user/preferences/$pid",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }
}
