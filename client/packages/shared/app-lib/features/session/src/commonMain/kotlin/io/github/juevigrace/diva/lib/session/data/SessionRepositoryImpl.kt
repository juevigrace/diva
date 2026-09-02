package io.github.juevigrace.diva.lib.session.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.database.session.SessionStorage
import io.github.juevigrace.diva.lib.models.session.Session
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SessionRepositoryImpl(
    override val client: DivaClient,
    private val storage: SessionStorage,
) : SessionRepository {
    override fun getSessions(): Flow<Result<List<Session>>> = storage.getAllFlow()

    override fun getCurrentSession(): Flow<Result<Option<Session>>> = storage.getCurrentFlow()

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun markCurrent(id: Uuid): Result<Unit> = storage.markCurrent(id)

    override suspend fun delete(id: Uuid): Result<Unit> = storage.delete(id)

    override suspend fun deleteAll(): Result<Unit> = storage.deleteAll()
}
