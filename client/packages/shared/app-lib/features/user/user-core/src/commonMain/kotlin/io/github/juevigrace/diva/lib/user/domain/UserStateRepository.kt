package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.state.models.UserState

interface UserStateRepository : Repository {
    suspend fun get(id: String): Result<UserState>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(state: UserState): Result<Unit>
}
