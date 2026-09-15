package io.github.juevigrace.diva.lib.devices.data.api.client

import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.api.device.DeviceResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.getAs

interface DevicesApi {
    suspend fun listAll(token: String): Result<List<DeviceResponse>>
}

class DevicesApiImpl(
    private val client: DivaClient,
) : DevicesApi {
    override suspend fun listAll(token: String): Result<List<DeviceResponse>> {
        return client.getAs<ApiResponse<List<DeviceResponse>>>(
            path = "/api/devices",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data ?: error(it.message) }
    }
}