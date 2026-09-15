package io.github.juevigrace.diva.lib.database.session

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.session.Session
import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    suspend fun findAll(): Result<List<Session>>

    fun findAllFlow(): Flow<Result<List<Session>>>

    suspend fun findOne(id: String): Result<Option<Session>>

    fun findOneFlow(id: String): Flow<Result<Option<Session>>>

    suspend fun findCurrent(): Result<Option<Session>>

    fun findCurrentFlow(): Flow<Result<Option<Session>>>

    suspend fun upsert(item: Session): Result<Unit>

    suspend fun deleteOne(id: String): Result<Unit>

    suspend fun markCurrent(id: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}
