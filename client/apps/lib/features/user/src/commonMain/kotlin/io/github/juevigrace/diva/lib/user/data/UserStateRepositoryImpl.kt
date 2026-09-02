package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.database.user.state.UserStateStorage
import io.github.juevigrace.diva.lib.models.user.state.UserState
import io.github.juevigrace.diva.lib.user.domain.UserStateRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserStateRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserStateStorage,
) : UserStateRepository {
    override fun getState(userId: Uuid): Flow<Result<Option<UserState>>> = storage.getByUserFlow(userId)

    override suspend fun sync(userId: Uuid): Result<Unit> = Result.success(Unit)

    override suspend fun save(state: UserState): Result<Unit> = storage.upsert(state)
}
