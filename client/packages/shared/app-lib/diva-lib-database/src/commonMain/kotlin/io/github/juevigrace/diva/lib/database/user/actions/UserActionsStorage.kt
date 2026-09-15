package io.github.juevigrace.diva.lib.database.user.actions

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.actions.Actions
import io.github.juevigrace.diva.lib.models.user.actions.UserAction
import kotlinx.coroutines.flow.Flow

interface UserActionsStorage {
    suspend fun findAll(userId: String): Result<List<UserAction>>

    fun findAllFlow(userId: String): Flow<Result<List<UserAction>>>

    suspend fun findOne(id: String): Result<Option<UserAction>>

    fun findOneFlow(id: String): Flow<Result<Option<UserAction>>>

    suspend fun findByAction(userId: String, action: Actions): Result<Option<UserAction>>

    fun findByActionFlow(userId: String, action: Actions): Flow<Result<Option<UserAction>>>

    suspend fun upsert(userId: String, item: UserAction): Result<Unit>

    suspend fun deleteOne(id: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}
