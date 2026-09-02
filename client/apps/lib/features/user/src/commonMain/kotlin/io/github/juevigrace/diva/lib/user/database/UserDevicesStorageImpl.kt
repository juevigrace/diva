package io.github.juevigrace.diva.lib.user.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.DivaDB
import io.github.juevigrace.diva.lib.database.user.devices.UserDevicesStorage
import io.github.juevigrace.diva.lib.models.device.Device
import io.github.juevigrace.diva.lib.models.user.device.UserDevice
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserDevicesStorageImpl(
    private val db: DivaDatabase<DivaDB>
) : UserDevicesStorage {

    override suspend fun getAllByUser(userId: Uuid): Result<List<UserDevice>> {
        return db.getList {
            userDevicesQueries.findAllByUser(userId.toString(), ::mapToUserDevice)
        }
    }

    override suspend fun getById(userId: Uuid, deviceId: Uuid): Result<Option<UserDevice>> {
        return db.getOne {
            userDevicesQueries.findOneById(userId.toString(), deviceId.toString(), ::mapToUserDevice)
        }
    }

    override suspend fun upsert(item: UserDevice): Result<Unit> {
        return db.use {
            transaction {
                userDevicesQueries.upsert(
                    user_id = item.userId.toString(),
                    device_id = item.device.id.toString(),
                    created_at = item.createdAt.epochSeconds,
                    updated_at = item.updatedAt.epochSeconds
                )
            }
        }
    }

    override suspend fun delete(userId: Uuid, deviceId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                userDevicesQueries.deleteById(userId.toString(), deviceId.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                userDevicesQueries.deleteAll()
            }
        }
    }

    private fun mapToUserDevice(
        userId: String,
        deviceId: String,
        createdAt: Long,
        updatedAt: Long,
    ): UserDevice = UserDevice(
        userId = Uuid.parse(userId),
        device = Device(
            id = Uuid.parse(deviceId),
            name = "",
            createdAt = Instant.fromEpochSeconds(0),
            updatedAt = Instant.fromEpochSeconds(0)
        ),
        createdAt = Instant.fromEpochSeconds(createdAt),
        updatedAt = Instant.fromEpochSeconds(updatedAt)
    )
}
