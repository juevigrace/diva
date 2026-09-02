package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.device.UserDevice
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface UserDevicesRepository : Repository {
    val client: DivaClient

    fun getDevices(userId: Uuid): Flow<Result<List<UserDevice>>>

    fun getDevice(userId: Uuid, deviceId: Uuid): Flow<Result<Option<UserDevice>>>

    suspend fun sync(userId: Uuid): Result<Unit>

    suspend fun save(device: UserDevice): Result<Unit>

    suspend fun delete(userId: Uuid, deviceId: Uuid): Result<Unit>
}
