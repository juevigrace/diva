package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.actions.Actions
import io.github.juevigrace.diva.lib.models.user.actions.UserAction
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserActionsRepository : Repository {
    val client: DivaClient

    fun getActions(userId: String): Flow<Result<List<UserAction>>>

    fun getAction(userId: String, action: Actions): Flow<Result<Option<UserAction>>>

    suspend fun sync(userId: String): Result<Unit>

    suspend fun save(userId: String, action: UserAction): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
