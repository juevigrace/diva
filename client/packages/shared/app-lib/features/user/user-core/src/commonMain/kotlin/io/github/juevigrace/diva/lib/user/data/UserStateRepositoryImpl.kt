package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.data.api.client.UserStateApi
import io.github.juevigrace.diva.lib.user.database.state.UserStateStorage
import io.github.juevigrace.diva.lib.user.domain.UserStateRepository
import io.github.juevigrace.diva.lib.user.state.models.UserState

class UserStateRepositoryImpl(
    private val storage: UserStateStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserStateApi,
) : UserStateRepository {
    override suspend fun get(id: String): Result<UserState> = storage.findOne(id).mapCatching { option ->
        option.getOrThrow { IllegalStateException("No user state for user '$id'") }
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
