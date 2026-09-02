package io.github.juevigrace.diva.lib.database.user.devices

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.device.UserDevice
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserDevicesStorage {
    suspend fun getAllByUser(userId: Uuid): Result<List<UserDevice>>

    fun getAllByUserFlow(userId: Uuid): Flow<Result<List<UserDevice>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(userId: Uuid, deviceId: Uuid): Result<Option<UserDevice>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByIdFlow(userId: Uuid, deviceId: Uuid): Flow<Result<Option<UserDevice>>>

    suspend fun upsert(item: UserDevice): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(userId: Uuid, deviceId: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
