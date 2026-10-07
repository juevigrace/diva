package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.device.models.UserDevice
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserDevicesRepository : Repository {
    val client: DivaClient

    fun getDevices(): Flow<Result<List<UserDevice>>>

    fun getDevice(deviceId: String): Flow<Result<UserDevice>>

    suspend fun sync(): Result<Unit>

    suspend fun save(device: UserDevice): Result<Unit>

    suspend fun delete(userId: String, deviceId: String): Result<Unit>
}
