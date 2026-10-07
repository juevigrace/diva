package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.state.models.UserState
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserStateRepository : Repository {
    val client: DivaClient

    fun observe(): Flow<Result<UserState>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(state: UserState): Result<Unit>
}
