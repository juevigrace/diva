package io.github.juevigrace.diva.lib.session.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.lib.database.session.SessionStorage
import io.github.juevigrace.diva.lib.models.session.Session
import io.github.juevigrace.diva.lib.session.data.api.client.SessionsApi
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SessionRepositoryImpl(
    override val client: DivaClient,
    private val storage: SessionStorage,
    private val api: SessionsApi,
) : SessionRepository {
    override fun getSessions(): Flow<Result<List<Session>>> = storage.getAllFlow()

    override fun getCurrentSession(): Flow<Result<Option<Session>>> = storage.getCurrentFlow()

    override suspend fun getCurrent(): Result<Session> {
        return storage.getCurrent().mapCatching { option ->
            option.getOrThrow { error("No current session available") }
        }
    }

    override suspend fun sync(): Result<Unit> {
        return withSession(
            sessionCall = this::getCurrent,
            onFound = { current ->
                api.listAll(current.accessToken).mapCatching { responses ->
                    val results = responses.map {
                        scope.async {
                            storage.upsert(Session.fromResponse(it))
                        }
                    }.awaitAll()

                    val failures = results.mapNotNull { it.exceptionOrNull() }
                    if (failures.isNotEmpty()) {
                        val error = IllegalStateException(
                            "Failed to upsert ${failures.size} of ${results.size} sessions"
                        )
                        failures.forEach(error::addSuppressed)
                        throw error
                    }
                }
            },
        )
    }

    override suspend fun markCurrent(id: Uuid): Result<Unit> = storage.markCurrent(id)

    override suspend fun delete(id: Uuid): Result<Unit> = storage.delete(id)

    override suspend fun deleteAll(): Result<Unit> = storage.deleteAll()
}
