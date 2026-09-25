package com.diva.app.player.database

import com.diva.app.models.player.PlayerSetting
import com.diva.app.models.player.RepeatMode
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow

interface PlayerSettingStorage {
    suspend fun getByUser(userId: String): Result<Option<PlayerSetting>>

    fun getByUserFlow(userId: String): Flow<Result<Option<PlayerSetting>>>

    suspend fun upsert(item: PlayerSetting): Result<Unit>

    suspend fun deleteByUser(userId: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}

class PlayerSettingStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : PlayerSettingStorage {

    override suspend fun getByUser(userId: String): Result<Option<PlayerSetting>> {
        return db.getOne { playerSettingQueries.findByUser(userId, ::mapToSetting) }
    }

    override fun getByUserFlow(userId: String): Flow<Result<Option<PlayerSetting>>> {
        return db.getOneAsFlow { playerSettingQueries.findByUser(userId, ::mapToSetting) }
    }

    override suspend fun upsert(item: PlayerSetting): Result<Unit> {
        return db.use {
            transaction {
                playerSettingQueries.upsert(
                    user_id = item.userId,
                    volume = item.volume.toDouble(),
                    playback_speed = item.playbackSpeed.toDouble(),
                    repeat_mode = item.repeatMode,
                    shuffle = item.shuffle,
                    updated_at = item.updatedAt,
                )
            }
        }
    }

    override suspend fun deleteByUser(userId: String): Result<Unit> {
        return db.use {
            transaction {
                playerSettingQueries.deleteByUser(userId)
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
        userId = userId,
        volume = volume.toFloat(),
        playbackSpeed = playbackSpeed.toFloat(),
        repeatMode = repeatMode,
        shuffle = shuffle,
        updatedAt = updatedAt,
    )
}
