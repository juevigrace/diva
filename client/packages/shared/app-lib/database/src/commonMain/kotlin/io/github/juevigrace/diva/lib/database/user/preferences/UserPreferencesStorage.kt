package io.github.juevigrace.diva.lib.database.user.preferences

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserPreferencesStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(id: Uuid): Result<Option<UserPreferences>>

    suspend fun getByUser(userId: Uuid): Result<Option<UserPreferences>>

    fun getByUserFlow(userId: Uuid): Flow<Result<Option<UserPreferences>>>

    suspend fun upsert(item: UserPreferences): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
