package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.actions.models.UserAction
import io.github.juevigrace.diva.lib.user.data.api.client.UserActionsApi
import io.github.juevigrace.diva.lib.user.database.actions.UserActionsStorage
import io.github.juevigrace.diva.lib.user.domain.UserActionsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class UserActionsRepositoryImpl(
    private val storage: UserActionsStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserActionsApi,
) : UserActionsRepository {
    override suspend fun get(id: String): Result<List<UserAction>> = storage.findAll(id)

    override suspend fun sync(): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::get,
            onFound = { session ->
                api.list(
                    uid = session.userId,
                    token = session.accessToken
                ).mapCatching { responses ->
                    val results = responses.map {
                        scope.async {
                            storage.upsert(session.userId, UserAction.fromResponse(it))
                        }
                    }.awaitAll()

                    val failures = results.mapNotNull { it.exceptionOrNull() }
                    if (failures.isNotEmpty()) {
                        val error = IllegalStateException(
                            "Failed to upsert ${failures.size} of ${results.size} user actions"
                        )
                        failures.forEach(error::addSuppressed)
                        throw error
                    }
                }
            },
        )
    }

    override suspend fun upsert(action: UserAction): Result<Unit> = withSession(sessionRepository::get) { session ->
        storage.upsert(session.userId, action)
    }

    override suspend fun delete(id: String): Result<Unit> = storage.deleteOne(id)
}
