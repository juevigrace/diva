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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserActionsRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserActionsStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserActionsApi,
) : UserActionsRepository {
    override fun getActions(userId: Uuid): Flow<Result<List<UserAction>>> = storage.getAllByUserFlow(userId)

    override fun getAction(userId: Uuid, action: Actions): Flow<Result<Option<UserAction>>> =
        storage.getByActionFlow(userId, action)

    override suspend fun sync(userId: Uuid): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::getCurrent,
            onFound = { session ->
                api.list(
                    uid = userId.toString(),
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

    override suspend fun save(userId: Uuid, action: UserAction): Result<Unit> = storage.upsert(userId, action)

    override suspend fun delete(id: Uuid): Result<Unit> = storage.delete(id)
}
