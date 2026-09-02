package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.database.user.devices.UserDevicesStorage
import io.github.juevigrace.diva.lib.models.user.device.UserDevice
import io.github.juevigrace.diva.lib.user.domain.UserDevicesRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserDevicesRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserDevicesStorage,
) : UserDevicesRepository {
    override fun getDevices(userId: Uuid): Flow<Result<List<UserDevice>>> = storage.getAllByUserFlow(userId)

    override fun getDevice(userId: Uuid, deviceId: Uuid): Flow<Result<Option<UserDevice>>> =
        storage.getByIdFlow(userId, deviceId)

    override suspend fun sync(userId: Uuid): Result<Unit> = Result.success(Unit)

    override suspend fun save(device: UserDevice): Result<Unit> = storage.upsert(device)

    override suspend fun delete(userId: Uuid, deviceId: Uuid): Result<Unit> = storage.delete(userId, deviceId)
}
