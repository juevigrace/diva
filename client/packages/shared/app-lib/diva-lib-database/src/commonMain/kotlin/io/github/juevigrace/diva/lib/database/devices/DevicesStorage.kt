package io.github.juevigrace.diva.lib.database.devices

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.device.Device
import kotlinx.coroutines.flow.Flow

interface DevicesStorage {
    suspend fun findAll(): Result<List<Device>>

    fun findAllFlow(): Flow<Result<List<Device>>>

    suspend fun findOne(id: String): Result<Option<Device>>

    fun findOneFlow(id: String): Flow<Result<Option<Device>>>

    suspend fun upsert(item: Device): Result<Unit>

    suspend fun deleteOne(id: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}
