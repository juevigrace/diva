package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.database.user.state.UserStateStorage
import io.github.juevigrace.diva.lib.models.user.state.UserState
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.user.data.api.client.UserStateApi
import io.github.juevigrace.diva.lib.user.domain.UserStateRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

class UserStateRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserStateStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserStateApi,
) : UserStateRepository {
    override fun getState(userId: String): Flow<Result<Option<UserState>>> = storage.findOneFlow(userId)

    override suspend fun sync(userId: String): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::getCurrent,
            onFound = { session ->
                api.getState(
                    uid = userId,
                    token = session.accessToken
                ).mapCatching { option ->
                    option.map { storage.upsert(userId, UserState.fromResponse(it)).getOrThrow() }
                        .getOrDefault(Unit)
                }
            },
        )
    }

    override suspend fun save(userId: String, state: UserState): Result<Unit> = storage.upsert(userId, state)
}
