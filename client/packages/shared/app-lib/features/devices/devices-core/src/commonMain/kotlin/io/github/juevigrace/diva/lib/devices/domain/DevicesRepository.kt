package io.github.juevigrace.diva.lib.devices.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.device.models.Device
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface DevicesRepository : Repository {
    val client: DivaClient

    fun observe(): Flow<Result<List<Device>>>

    fun observe(id: String): Flow<Result<Device>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(device: Device): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
