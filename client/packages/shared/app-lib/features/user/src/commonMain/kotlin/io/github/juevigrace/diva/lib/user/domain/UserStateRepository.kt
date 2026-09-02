package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.state.UserState
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface UserStateRepository : Repository {
    val client: DivaClient

    fun getState(userId: Uuid): Flow<Result<Option<UserState>>>

    suspend fun sync(userId: Uuid): Result<Unit>

    suspend fun save(state: UserState): Result<Unit>
}
