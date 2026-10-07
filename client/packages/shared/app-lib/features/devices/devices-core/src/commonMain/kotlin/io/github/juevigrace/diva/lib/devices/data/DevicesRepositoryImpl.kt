package io.github.juevigrace.diva.lib.devices.data

import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.lib.device.models.Device
import io.github.juevigrace.diva.lib.devices.database.DevicesStorage
import io.github.juevigrace.diva.lib.devices.domain.DevicesRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DevicesRepositoryImpl(
    override val client: DivaClient,
    private val storage: DevicesStorage,
) : DevicesRepository {
    override fun observe(): Flow<Result<List<Device>>> = storage.findAllFlow()

    override fun observe(id: String): Flow<Result<Device>> = storage.findOneFlow(id).map { result ->
        result.mapCatching { option ->
            option.getOrThrow { IllegalStateException("No device '$id'") }
        }
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun upsert(device: Device): Result<Unit> = storage.upsert(device)

    override suspend fun delete(id: String): Result<Unit> = storage.deleteOne(id)
}
