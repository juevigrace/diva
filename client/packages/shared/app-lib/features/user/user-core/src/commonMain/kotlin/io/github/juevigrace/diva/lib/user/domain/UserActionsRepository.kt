package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.actions.models.Actions
import io.github.juevigrace.diva.lib.user.actions.models.UserAction
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserActionsRepository : Repository {
    val client: DivaClient

    fun observe(): Flow<Result<List<UserAction>>>

    fun observe(action: Actions): Flow<Result<UserAction>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(action: UserAction): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
