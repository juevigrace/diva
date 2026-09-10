package com.diva.app.features.player.database

import com.diva.app.database.DivaDB
import com.diva.app.database.player.PlayerSettingStorage
import com.diva.app.models.player.PlayerSetting
import com.diva.app.models.player.RepeatMode
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class PlayerSettingStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : PlayerSettingStorage {

    override suspend fun getByUser(userId: Uuid): Result<Option<PlayerSetting>> {
        return db.getOne { playerSettingQueries.findByUser(userId.toString(), ::mapToSetting) }
    }

    override fun getByUserFlow(userId: Uuid): Flow<Result<Option<PlayerSetting>>> {
        return db.getOneAsFlow { playerSettingQueries.findByUser(userId.toString(), ::mapToSetting) }
    }

    override suspend fun upsert(item: PlayerSetting): Result<Unit> {
        return db.use {
            transaction {
                playerSettingQueries.upsert(
                    user_id = item.userId.toString(),
                    volume = item.volume.toDouble(),
                    playback_speed = item.playbackSpeed.toDouble(),
                    repeat_mode = item.repeatMode,
                    shuffle = item.shuffle,
                    updated_at = item.updatedAt.epochSeconds,
                )
            }
        }
    }

    override suspend fun deleteByUser(userId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                playerSettingQueries.deleteByUser(userId.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                playerSettingQueries.deleteAll()
            }
        }
    }

    private fun mapToSetting(
        userId: String,
        volume: Double,
        playbackSpeed: Double,
        repeatMode: RepeatMode,
        shuffle: Boolean,
        updatedAt: Long,
    ): PlayerSetting = PlayerSetting(
        userId = Uuid.parse(userId),
        volume = volume.toFloat(),
        playbackSpeed = playbackSpeed.toFloat(),
        repeatMode = repeatMode,
        shuffle = shuffle,
        updatedAt = Instant.fromEpochSeconds(updatedAt),
    )
}