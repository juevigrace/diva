package io.github.juevigrace.diva.lib.database.user.devices

import io.github.juevigrace.diva.core.Option
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
