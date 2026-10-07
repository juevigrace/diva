package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.data.api.client.UserStateApi
import io.github.juevigrace.diva.lib.user.database.state.UserStateStorage
import io.github.juevigrace.diva.lib.user.domain.UserStateRepository
import io.github.juevigrace.diva.lib.user.state.models.UserState
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserStateRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserStateStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserStateApi,
) : UserStateRepository {
    override fun observe(): Flow<Result<UserState>> = observeSession(sessionRepository::observe) { session ->
        storage.findOneFlow(session.userId).map { result ->
            result.mapCatching { option ->
                option.getOrThrow { IllegalStateException("No user state for user '${session.userId}'") }
            }
        }
    }

    override suspend fun sync(): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::get,
            onFound = { session ->
                api.getState(
                    uid = session.userId,
                    token = session.accessToken
                ).mapCatching { option ->
                    option.map { storage.upsert(session.userId, UserState.fromResponse(it)).getOrThrow() }
                        .getOrDefault(Unit)
                }
            },
        )
    }

    override suspend fun upsert(state: UserState): Result<Unit> = withSession(sessionRepository::get) { session ->
        storage.upsert(session.userId, state)
    }
}
