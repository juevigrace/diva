package io.github.juevigrace.diva.lib.database.session

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.session.Session
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface SessionStorage {
    suspend fun getAll(): Result<List<Session>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(id: Uuid): Result<Option<Session>>

    suspend fun getCurrent(): Result<Option<Session>>

    fun getCurrentFlow(): Flow<Result<Option<Session>>>

    suspend fun upsert(item: Session): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun markCurrent(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
