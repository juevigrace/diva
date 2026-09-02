package io.github.juevigrace.diva.lib.database.user.actions

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.actions.Actions
import io.github.juevigrace.diva.lib.models.user.actions.UserAction
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserActionsStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(id: Uuid): Result<Option<UserAction>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByIdFlow(id: Uuid): Flow<Result<Option<UserAction>>>

    suspend fun getAllByUser(userId: Uuid): Result<List<UserAction>>

    fun getAllByUserFlow(userId: Uuid): Flow<Result<List<UserAction>>>

    suspend fun getByAction(userId: Uuid, action: Actions): Result<Option<UserAction>>

    fun getByActionFlow(userId: Uuid, action: Actions): Flow<Result<Option<UserAction>>>

    suspend fun upsert(item: UserAction): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
