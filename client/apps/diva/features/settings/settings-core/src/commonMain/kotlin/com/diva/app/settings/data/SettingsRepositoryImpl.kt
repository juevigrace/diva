package com.diva.app.settings.data

import com.diva.app.settings.database.SettingsStorage
import com.diva.app.settings.domain.SettingsRepository
import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.settings.models.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val storage: SettingsStorage,
    private val sRepository: SessionRepository,
) : SettingsRepository {
    override suspend fun get(): Result<AppSettings> {
        return withSession(sRepository::get) { s ->
            storage.getByUser(s.userId).mapCatching {
                it.getOrThrow { IllegalStateException("No settings for user '${s.userId}'") }
            }
        }
    }

    override fun observe(): Flow<Result<AppSettings>> {
        return observeSession(sRepository::observe) { s ->
            storage.getByUserFlow(s.userId).map { result ->
                result.mapCatching { opt ->
                    opt.getOrThrow { IllegalStateException("No settings for user '${s.userId}'") }
                }
            }
        }
    }

    override suspend fun upsert(settings: AppSettings): Result<Unit> {
        return withSession(sRepository::get) { s ->
            storage.upsert(s.userId, settings)
        }
    }
}
