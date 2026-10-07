package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.device.models.UserDevice
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserDevicesRepository : Repository {
    val client: DivaClient

    fun observe(): Flow<Result<List<UserDevice>>>

    fun observe(deviceId: String): Flow<Result<UserDevice>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(device: UserDevice): Result<Unit>

    suspend fun delete(deviceId: String): Result<Unit>
}
