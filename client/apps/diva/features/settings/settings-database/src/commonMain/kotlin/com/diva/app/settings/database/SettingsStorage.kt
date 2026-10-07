package com.diva.app.settings.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.settings.models.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsStorage {
    suspend fun getByUser(userId: String): Result<Option<AppSettings>>

    fun getByUserFlow(userId: String): Flow<Result<Option<AppSettings>>>

    suspend fun upsert(userId: String, item: AppSettings): Result<Unit>

    suspend fun deleteByUser(userId: String): Result<Unit>
}
