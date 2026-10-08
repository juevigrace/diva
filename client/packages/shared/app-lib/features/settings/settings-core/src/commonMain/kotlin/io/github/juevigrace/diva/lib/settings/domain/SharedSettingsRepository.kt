package io.github.juevigrace.diva.lib.settings.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.settings.models.Settings
import kotlinx.coroutines.flow.Flow

interface SharedSettingsRepository<S : Settings> : Repository {
    suspend fun get(): Result<S>

    fun observe(): Flow<Result<S>>

    suspend fun upsert(settings: S): Result<Unit>

    // TODO: delete function, also configure all delete functions for other tables by user
    // to delete all user related data when changing from the default user or logging out
}
