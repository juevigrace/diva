package io.github.juevigrace.diva.lib.devices.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.DivaSharedDB
import io.github.juevigrace.diva.lib.database.devices.DevicesStorage
import io.github.juevigrace.diva.lib.models.device.Device
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DevicesStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>,
) : DevicesStorage {

    override suspend fun getAll(): Result<List<Device>> {
        return db.getList {
            devicesQueries.findAll(::mapToDevice)
        }
    }

    override fun getAllFlow(): Flow<Result<List<Device>>> {
        return db.getListAsFlow {
            devicesQueries.findAll(::mapToDevice)
        }
    }

    override suspend fun getById(id: Uuid): Result<Option<Device>> {
        return db.getOne {
            devicesQueries.findOneById(id.toString(), ::mapToDevice)
        }
    }

    override fun getByIdFlow(id: Uuid): Flow<Result<Option<Device>>> {
        return db.getOneAsFlow {
            devicesQueries.findOneById(id.toString(), ::mapToDevice)
        }
    }

    override suspend fun upsert(item: Device): Result<Unit> {
        return db.use {
            transaction {
                devicesQueries.upsert(
                    id = item.id.toString(),
                    name = item.name,
                    created_at = item.createdAt.epochSeconds,
                    updated_at = item.updatedAt.epochSeconds
                )
            }
        }
    }

    override suspend fun delete(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                devicesQueries.deleteById(id.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                devicesQueries.deleteAll()
            }
        }
    }

    private fun mapToDevice(
        id: String,
        name: String,
        createdAt: Long,
        updatedAt: Long,
    ): Device = Device(
        id = Uuid.parse(id),
        name = name,
        createdAt = Instant.fromEpochSeconds(createdAt),
        updatedAt = Instant.fromEpochSeconds(updatedAt)
    )
}
