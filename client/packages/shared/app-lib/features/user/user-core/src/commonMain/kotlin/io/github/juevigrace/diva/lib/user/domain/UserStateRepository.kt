package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.state.UserState
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserStateRepository : Repository {
    val client: DivaClient

    fun getState(userId: String): Flow<Result<Option<UserState>>>

    suspend fun sync(userId: String): Result<Unit>

    suspend fun save(userId: String, state: UserState): Result<Unit>
}
