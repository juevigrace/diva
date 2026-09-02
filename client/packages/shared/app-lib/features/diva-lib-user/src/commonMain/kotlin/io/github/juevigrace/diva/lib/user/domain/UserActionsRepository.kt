package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.actions.Actions
import io.github.juevigrace.diva.lib.models.user.actions.UserAction
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface UserActionsRepository : Repository {
    val client: DivaClient

    fun getActions(userId: Uuid): Flow<Result<List<UserAction>>>

    fun getAction(userId: Uuid, action: Actions): Flow<Result<Option<UserAction>>>

    suspend fun sync(userId: Uuid): Result<Unit>

    suspend fun save(action: UserAction): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>
}
