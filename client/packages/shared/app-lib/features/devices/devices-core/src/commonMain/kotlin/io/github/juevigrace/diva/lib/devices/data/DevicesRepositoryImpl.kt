package io.github.juevigrace.diva.lib.devices.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.devices.database.DevicesStorage
import io.github.juevigrace.diva.lib.devices.domain.DevicesRepository
import io.github.juevigrace.diva.lib.models.device.Device
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

class DevicesRepositoryImpl(
    override val client: DivaClient,
    private val storage: DevicesStorage,
) : DevicesRepository {
    override fun getDevices(): Flow<Result<List<Device>>> = storage.findAllFlow()

    override fun getDevice(id: String): Flow<Result<Option<Device>>> = storage.findOneFlow(id)

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(device: Device): Result<Unit> = storage.upsert(device)

    override suspend fun delete(id: String): Result<Unit> = storage.deleteOne(id)
}
