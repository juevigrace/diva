package io.github.juevigrace.diva.lib.settings.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.settings.models.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository : Repository {
    fun observe(userId: String): Flow<Result<Option<AppSettings>>>

    suspend fun upsert(userId: String, settings: AppSettings): Result<Unit>

    // TODO: delete function, also configure all delete functions for other tables by user
    // to delete all user related data when changing from the default user or logging out
}
