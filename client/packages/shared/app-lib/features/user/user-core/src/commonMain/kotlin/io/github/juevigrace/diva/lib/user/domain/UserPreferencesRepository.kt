package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.preferences.models.UserPreferences

interface UserPreferencesRepository : Repository {
    suspend fun get(id: String): Result<UserPreferences>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(preferences: UserPreferences): Result<Unit>

    suspend fun delete(userId: String): Result<Unit>
}
