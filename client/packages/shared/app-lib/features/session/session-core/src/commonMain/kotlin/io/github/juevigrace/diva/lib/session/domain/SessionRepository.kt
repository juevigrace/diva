package io.github.juevigrace.diva.lib.session.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.session.models.Session
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface SessionRepository : Repository {
    val client: DivaClient

    suspend fun get(): Result<Session>

    fun observe(): Flow<Result<Session>>

    fun getSessions(): Flow<Result<List<Session>>>

    suspend fun upsert(session: Session): Result<Unit>

    suspend fun sync(): Result<Unit>

    suspend fun markCurrent(id: String): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
