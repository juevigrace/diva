package io.github.juevigrace.diva.lib.database.user.state

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.state.UserState
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface UserStateStorage {
    suspend fun getByUser(userId: Uuid): Result<Option<UserState>>

    fun getByUserFlow(userId: Uuid): Flow<Result<Option<UserState>>>

    suspend fun upsert(item: UserState): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
