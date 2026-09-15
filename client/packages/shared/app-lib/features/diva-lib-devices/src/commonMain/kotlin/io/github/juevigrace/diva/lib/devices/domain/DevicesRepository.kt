package io.github.juevigrace.diva.lib.devices.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.device.Device
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface DevicesRepository : Repository {
    val client: DivaClient

    fun getDevices(): Flow<Result<List<Device>>>

    fun getDevice(id: String): Flow<Result<Option<Device>>>

    suspend fun sync(): Result<Unit>

    suspend fun save(device: Device): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
