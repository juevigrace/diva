package io.github.juevigrace.diva.lib.user.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.user.device.UserDeviceResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.deleteAs
import io.github.juevigrace.diva.network.client.getAs

interface UserDevicesApi {
    suspend fun list(uid: String, token: String): Result<List<UserDeviceResponse>>
    suspend fun getByID(uid: String, did: String, token: String): Result<UserDeviceResponse>
    suspend fun delete(uid: String, did: String, token: String): Result<Unit>
}

class UserDevicesApiImpl(
    private val client: DivaClient,
) : UserDevicesApi {
    override suspend fun list(uid: String, token: String): Result<List<UserDeviceResponse>> {
        return client.getAs<ApiResponse<List<UserDeviceResponse>>>(
            path = "/api/user/$uid/devices",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error(it.message) }
    }

    override suspend fun getByID(uid: String, did: String, token: String): Result<UserDeviceResponse> {
        return client.getAs<ApiResponse<UserDeviceResponse>>(
            path = "/api/user/$uid/devices/$did",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error(it.message) }
    }

    override suspend fun delete(uid: String, did: String, token: String): Result<Unit> {
        return client.deleteAs<Unit>(
            path = "/api/user/$uid/devices/$did",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }
}
