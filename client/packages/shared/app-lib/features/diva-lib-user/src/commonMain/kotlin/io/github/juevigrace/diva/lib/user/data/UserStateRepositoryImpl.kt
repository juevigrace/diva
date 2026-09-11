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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserStateRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserStateStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserStateApi,
) : UserStateRepository {
    override fun getState(userId: Uuid): Flow<Result<Option<UserState>>> = storage.getByUserFlow(userId)

    override suspend fun sync(userId: Uuid): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::getCurrent,
            onFound = { session ->
                api.getState(
                    uid = userId.toString(),
                    token = session.accessToken
                ).mapCatching { option ->
                    option.map { storage.upsert(userId, UserState.fromResponse(it)).getOrThrow() }
                        .getOrDefault(Unit)
                }
            },
        )
    }

    override suspend fun save(userId: Uuid, state: UserState): Result<Unit> = storage.upsert(userId, state)
}
