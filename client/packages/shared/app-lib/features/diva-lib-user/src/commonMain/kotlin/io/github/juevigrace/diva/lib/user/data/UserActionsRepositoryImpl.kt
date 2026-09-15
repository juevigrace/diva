package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.database.user.actions.UserActionsStorage
import io.github.juevigrace.diva.lib.models.actions.Actions
import io.github.juevigrace.diva.lib.models.user.actions.UserAction
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.user.data.api.client.UserActionsApi
import io.github.juevigrace.diva.lib.user.domain.UserActionsRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow

class UserActionsRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserActionsStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserActionsApi,
) : UserActionsRepository {
    override fun getActions(userId: String): Flow<Result<List<UserAction>>> = storage.findAllFlow(userId)

    override fun getAction(userId: String, action: Actions): Flow<Result<Option<UserAction>>> =
        storage.findByActionFlow(userId, action)

    override suspend fun sync(userId: String): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::getCurrent,
            onFound = { session ->
                api.list(
                    uid = userId,
                    token = session.accessToken
                ).mapCatching { responses ->
                    val results = responses.map {
                        scope.async {
                            storage.upsert(userId, UserAction.fromResponse(it))
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

    override suspend fun save(userId: String, action: UserAction): Result<Unit> = storage.upsert(userId, action)

    override suspend fun delete(id: String): Result<Unit> = storage.deleteOne(id)
}
