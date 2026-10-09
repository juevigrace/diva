package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.device.models.UserDevice

interface UserDevicesRepository : Repository {
    suspend fun get(id: String): Result<List<UserDevice>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(device: UserDevice): Result<Unit>

    suspend fun delete(deviceId: String): Result<Unit>
}
