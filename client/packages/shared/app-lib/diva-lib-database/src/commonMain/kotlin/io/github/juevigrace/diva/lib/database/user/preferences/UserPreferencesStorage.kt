package io.github.juevigrace.diva.lib.database.user.preferences

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesStorage {
    suspend fun findOne(userId: String): Result<Option<UserPreferences>>

    fun findOneFlow(userId: String): Flow<Result<Option<UserPreferences>>>

    suspend fun upsert(userId: String, item: UserPreferences): Result<Unit>

    suspend fun deleteOne(userId: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}
