package com.diva.app.settings.database

import com.diva.app.settings.models.DivaSettings
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.settings.models.AppSettings
import kotlinx.coroutines.flow.Flow

class SettingsStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : SettingsStorage {

    override suspend fun getByUser(userId: String): Result<Option<AppSettings>> {
        return db.getOne { settingsQueries.findByUser(userId, ::mapToAppSettings) }
    }

    override fun getByUserFlow(userId: String): Flow<Result<Option<AppSettings>>> {
        return db.getOneAsFlow { settingsQueries.findByUser(userId, ::mapToAppSettings) }
    }

    override suspend fun upsert(userId: String, item: AppSettings): Result<Unit> {
        return db.use {
            transaction {
                settingsQueries.upsert(
                    user_id = userId,
                    port = item.port.map { it.toLong() }.getOrNull(),
                    host = item.host,
                    is_desktop = item.isDesktop,
                    protocol = item.protocol,
                )
            }
        }
    }

    override suspend fun deleteByUser(userId: String): Result<Unit> {
        return db.use {
            transaction {
                settingsQueries.deleteByUser(userId)
            }
        }
    }

    private fun mapToAppSettings(
        userId: String,
        port: Long?,
        host: String,
        isDesktop: Boolean,
        protocol: String,
    ): AppSettings = DivaSettings(
        protocol = protocol,
        port = Option.of(port?.toInt()),
        host = host,
        isDesktop = isDesktop,
    )
}
