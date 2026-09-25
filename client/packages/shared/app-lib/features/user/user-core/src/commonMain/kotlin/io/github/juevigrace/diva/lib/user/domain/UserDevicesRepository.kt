package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.device.UserDevice
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserDevicesRepository : Repository {
    val client: DivaClient

    fun getDevices(userId: String): Flow<Result<List<UserDevice>>>

    fun getDevice(userId: String, deviceId: String): Flow<Result<Option<UserDevice>>>

    suspend fun sync(userId: String): Result<Unit>

    suspend fun save(device: UserDevice): Result<Unit>

    suspend fun delete(userId: String, deviceId: String): Result<Unit>
}
