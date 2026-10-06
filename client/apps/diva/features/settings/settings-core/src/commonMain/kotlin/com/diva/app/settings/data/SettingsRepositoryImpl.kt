package com.diva.app.settings.data

import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.lib.settings.domain.SettingsRepository
import io.github.juevigrace.diva.lib.settings.models.AppSettings
import io.github.juevigrace.diva.lib.settings.models.SettingsStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val storage: SettingsStorage,
) : SettingsRepository {

    override suspend fun get(userId: String): Result<AppSettings> =
        storage.getByUser(userId).mapCatching {
            it.getOrThrow { IllegalStateException("No settings for user '$userId'") }
        }

    override fun observe(userId: String): Flow<Result<AppSettings>> =
        storage.getByUserFlow(userId).map { result ->
            result.mapCatching {
                it.getOrThrow { IllegalStateException("No settings for user '$userId'") }
            }
        }

    override suspend fun upsert(userId: String, settings: AppSettings): Result<Unit> =
        storage.upsert(userId, settings)
}
