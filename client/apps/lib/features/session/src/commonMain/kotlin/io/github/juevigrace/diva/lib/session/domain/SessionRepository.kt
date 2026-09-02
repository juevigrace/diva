package io.github.juevigrace.diva.lib.session.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.session.Session
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface SessionRepository : Repository {
    val client: DivaClient

    fun getSessions(): Flow<Result<List<Session>>>

    fun getCurrentSession(): Flow<Result<Option<Session>>>

    suspend fun sync(): Result<Unit>

    suspend fun markCurrent(id: Uuid): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
