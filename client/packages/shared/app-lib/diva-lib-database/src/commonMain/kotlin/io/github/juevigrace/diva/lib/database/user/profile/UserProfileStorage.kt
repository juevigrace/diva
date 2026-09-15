package io.github.juevigrace.diva.lib.database.user.profile

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.profile.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileStorage {
    suspend fun findOne(userId: String): Result<Option<UserProfile>>

    fun findOneFlow(userId: String): Flow<Result<Option<UserProfile>>>

    suspend fun upsert(userId: String, item: UserProfile): Result<Unit>

    suspend fun deleteOne(userId: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}
