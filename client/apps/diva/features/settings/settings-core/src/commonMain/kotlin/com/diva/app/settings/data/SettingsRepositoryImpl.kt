package com.diva.app.settings.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.settings.domain.SettingsRepository
import io.github.juevigrace.diva.lib.settings.models.AppSettings
import io.github.juevigrace.diva.lib.settings.models.SettingsStorage
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val storage: SettingsStorage,
) : SettingsRepository {

    override fun observe(userId: String): Flow<Result<Option<AppSettings>>> = storage.getByUserFlow(userId)

    override suspend fun upsert(userId: String, settings: AppSettings): Result<Unit> = storage.upsert(userId, settings)
}
