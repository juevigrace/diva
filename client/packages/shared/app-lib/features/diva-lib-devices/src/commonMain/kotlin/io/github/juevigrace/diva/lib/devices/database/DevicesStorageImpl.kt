package io.github.juevigrace.diva.lib.devices.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.DivaSharedDB
import io.github.juevigrace.diva.lib.database.devices.DevicesStorage
import io.github.juevigrace.diva.lib.models.device.Device
import kotlinx.coroutines.flow.Flow

class DevicesStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>,
) : DevicesStorage {

    override suspend fun findAll(): Result<List<Device>> {
        return db.getList {
            devicesQueries.findAll(::mapToDevice)
        }
    }

    override fun findAllFlow(): Flow<Result<List<Device>>> {
        return db.getListAsFlow {
            devicesQueries.findAll(::mapToDevice)
        }
    }

    override suspend fun findOne(id: String): Result<Option<Device>> {
        return db.getOne {
            devicesQueries.findOne(id, ::mapToDevice)
        }
    }

    override fun findOneFlow(id: String): Flow<Result<Option<Device>>> {
        return db.getOneAsFlow {
            devicesQueries.findOne(id, ::mapToDevice)
        }
    }

    override suspend fun upsert(item: Device): Result<Unit> {
        return db.use {
            transaction {
                devicesQueries.upsert(
                    id = item.id,
                    name = item.name,
                    created_at = item.createdAt,
                    updated_at = item.updatedAt
                )
            }
        }
    }

    override suspend fun deleteOne(id: String): Result<Unit> {
        return db.use {
            transaction {
                devicesQueries.deleteOne(id)
            }
        }
    }

    override suspend fun delete(): Result<Unit> {
        return db.use {
            transaction {
                devicesQueries.delete()
            }
        }
    }

    private fun mapToDevice(
        id: String,
        name: String,
        createdAt: Long,
        updatedAt: Long,
    ): Device = Device(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
