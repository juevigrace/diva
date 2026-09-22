package io.github.juevigrace.diva.lib.user.database.devices

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.user.DivaSharedDB
import io.github.juevigrace.diva.lib.models.device.Device
import io.github.juevigrace.diva.lib.models.user.device.UserDevice
import kotlinx.coroutines.flow.Flow

interface UserDevicesStorage {
    suspend fun findAll(userId: String): Result<List<UserDevice>>

    fun findAllFlow(userId: String): Flow<Result<List<UserDevice>>>

    suspend fun findOne(userId: String, deviceId: String): Result<Option<UserDevice>>

    fun findOneFlow(userId: String, deviceId: String): Flow<Result<Option<UserDevice>>>

    suspend fun upsert(item: UserDevice): Result<Unit>

    suspend fun deleteOne(userId: String, deviceId: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}

class UserDevicesStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>
) : UserDevicesStorage {

    override suspend fun findAll(userId: String): Result<List<UserDevice>> {
        return db.getList {
            userDevicesQueries.findAll(userId, ::mapToUserDevice)
        }
    }

    override fun findAllFlow(userId: String): Flow<Result<List<UserDevice>>> {
        return db.getListAsFlow {
            userDevicesQueries.findAll(userId, ::mapToUserDevice)
        }
    }

    override suspend fun findOne(userId: String, deviceId: String): Result<Option<UserDevice>> {
        return db.getOne {
            userDevicesQueries.findOne(userId, deviceId, ::mapToUserDevice)
        }
    }

    override fun findOneFlow(userId: String, deviceId: String): Flow<Result<Option<UserDevice>>> {
        return db.getOneAsFlow {
            userDevicesQueries.findOne(userId, deviceId, ::mapToUserDevice)
        }
    }

    override suspend fun upsert(item: UserDevice): Result<Unit> {
        return db.use {
            transaction {
                userDevicesQueries.upsert(
                    user_id = item.userId,
                    device_id = item.device.id,
                    created_at = item.createdAt,
                    updated_at = item.updatedAt
                )
            }
        }
    }

    override suspend fun deleteOne(userId: String, deviceId: String): Result<Unit> {
        return db.use {
            transaction {
                userDevicesQueries.deleteOne(userId, deviceId)
            }
        }
    }

    override suspend fun delete(): Result<Unit> {
        return db.use {
            transaction {
                userDevicesQueries.delete()
            }
        }
    }

    private fun mapToUserDevice(
        userId: String,
        deviceId: String,
        createdAt: Long,
        updatedAt: Long,
    ): UserDevice = UserDevice(
        userId = userId,
        device = Device(
            id = deviceId,
            name = "",
            createdAt = 0L,
            updatedAt = 0L
        ),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
