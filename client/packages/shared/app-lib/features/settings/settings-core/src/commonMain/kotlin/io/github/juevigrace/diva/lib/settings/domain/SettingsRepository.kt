package io.github.juevigrace.diva.lib.settings.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.settings.models.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository : Repository {
    suspend fun get(userId: String): Result<AppSettings>

    fun observe(userId: String): Flow<Result<AppSettings>>

    suspend fun upsert(userId: String, settings: AppSettings): Result<Unit>

    // TODO: delete function, also configure all delete functions for other tables by user
    // to delete all user related data when changing from the default user or logging out
}
