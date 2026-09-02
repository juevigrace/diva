package io.github.juevigrace.diva.lib.database.devices

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.device.Device
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface DevicesStorage {
    suspend fun getAll(): Result<List<Device>>

    fun getAllFlow(): Flow<Result<List<Device>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(id: Uuid): Result<Option<Device>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByIdFlow(id: Uuid): Flow<Result<Option<Device>>>

    suspend fun upsert(item: Device): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
