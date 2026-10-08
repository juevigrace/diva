package com.diva.app.settings.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.settings.models.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsStorage<S : Settings> {
    suspend fun getByUser(userId: String): Result<Option<S>>

    fun getByUserFlow(userId: String): Flow<Result<Option<S>>>

    suspend fun upsert(userId: String, item: S): Result<Unit>

    suspend fun deleteByUser(userId: String): Result<Unit>
}
