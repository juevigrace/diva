package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.preferences.models.UserPreferences
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository : Repository {
    val client: DivaClient

    fun observe(): Flow<Result<UserPreferences>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(preferences: UserPreferences): Result<Unit>

    suspend fun delete(userId: String): Result<Unit>
}
