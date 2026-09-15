package io.github.juevigrace.diva.lib.database.user.state

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.state.UserState
import kotlinx.coroutines.flow.Flow

interface UserStateStorage {
    suspend fun findOne(userId: String): Result<Option<UserState>>

    fun findOneFlow(userId: String): Flow<Result<Option<UserState>>>

    suspend fun upsert(userId: String, item: UserState): Result<Unit>

    suspend fun deleteOne(userId: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}
