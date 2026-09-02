package io.github.juevigrace.diva.lib.database.user.profile

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.profile.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface UserProfileStorage {
    suspend fun getByUser(userId: Uuid): Result<Option<UserProfile>>

    fun getByUserFlow(userId: Uuid): Flow<Result<Option<UserProfile>>>

    suspend fun upsert(item: UserProfile): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
