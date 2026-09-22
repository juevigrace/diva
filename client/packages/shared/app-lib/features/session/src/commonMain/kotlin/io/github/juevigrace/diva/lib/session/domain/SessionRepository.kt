package io.github.juevigrace.diva.lib.session.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.session.Session
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface SessionRepository : Repository {
    val client: DivaClient

    fun getSessions(): Flow<Result<List<Session>>>

    fun getCurrentSession(): Flow<Result<Option<Session>>>

    suspend fun getCurrent(): Result<Session>

    suspend fun sync(): Result<Unit>

    suspend fun markCurrent(id: String): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
