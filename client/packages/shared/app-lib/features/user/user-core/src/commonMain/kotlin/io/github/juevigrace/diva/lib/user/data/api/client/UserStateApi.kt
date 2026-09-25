package io.github.juevigrace.diva.lib.user.data.api.client

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.user.api.state.UpdateUserStatusDto
import io.github.juevigrace.diva.lib.models.user.api.state.UpdateVerifiedDto
import io.github.juevigrace.diva.lib.models.user.api.state.UserStateResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.getAs
import io.github.juevigrace.diva.network.client.patchAs
import io.github.juevigrace.diva.network.client.postAs
import io.github.juevigrace.diva.network.client.putAs

interface UserStateApi {
    suspend fun getState(uid: String, token: String): Result<Option<UserStateResponse>>
    suspend fun ping(uid: String, token: String): Result<Unit>
    suspend fun updateVerified(uid: String, dto: UpdateVerifiedDto, token: String): Result<Unit>
    suspend fun updateStatus(uid: String, dto: UpdateUserStatusDto, token: String): Result<Unit>
}

class UserStateApiImpl(
    private val client: DivaClient,
) : UserStateApi {
    override suspend fun getState(uid: String, token: String): Result<Option<UserStateResponse>> {
        return client.getAs<ApiResponse<UserStateResponse?>>(
            path = "/api/user/$uid/status",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data.toOption() }
    }

    override suspend fun ping(uid: String, token: String): Result<Unit> {
        return client.postAs<Unit>(
            path = "/api/user/$uid/status/ping",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun updateVerified(uid: String, dto: UpdateVerifiedDto, token: String): Result<Unit> {
        return client.patchAs<Unit>(
            path = "/api/user/$uid/status/verified",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun updateStatus(uid: String, dto: UpdateUserStatusDto, token: String): Result<Unit> {
        return client.putAs<Unit>(
            path = "/api/user/$uid/status",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }
}
