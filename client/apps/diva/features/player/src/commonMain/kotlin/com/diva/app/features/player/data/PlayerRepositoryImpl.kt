package com.diva.app.features.player.data

import com.diva.app.features.player.database.PlaybackHistoryStorage
import com.diva.app.features.player.database.PlayerSettingStorage
import com.diva.app.features.player.database.ResumePointStorage
import com.diva.app.features.player.domain.PlayerRepository
import com.diva.app.models.media.Media
import com.diva.app.models.playback.PlaybackHistory
import com.diva.app.models.playback.ResumePoint
import com.diva.app.models.player.PlayerSetting
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow

class PlayerRepositoryImpl(
    private val resumePointStorage: ResumePointStorage,
    private val historyStorage: PlaybackHistoryStorage,
    private val settingStorage: PlayerSettingStorage,
) : PlayerRepository {

    override suspend fun getResumePoint(userId: String, mediaId: String): Result<Option<ResumePoint>> {
        return resumePointStorage.getByUserAndMedia(userId, mediaId)
    }

    override suspend fun saveResumePoint(item: ResumePoint): Result<Unit> {
        return resumePointStorage.upsert(item)
    }

    override suspend fun deleteResumePoint(userId: String, mediaId: String): Result<Unit> {
        return resumePointStorage.delete(userId, mediaId)
    }

    override suspend fun deleteCompletedByUser(userId: String): Result<Unit> {
        return resumePointStorage.deleteCompletedByUser(userId)
    }

    override suspend fun getResumableByUser(userId: String): Result<List<Media>> {
        return resumePointStorage.getResumableByUser(userId)
    }

    override fun getResumableByUserFlow(userId: String): Flow<Result<List<Media>>> {
        return resumePointStorage.getResumableByUserFlow(userId)
    }

    override suspend fun recordPlayback(item: PlaybackHistory): Result<Unit> {
        return historyStorage.record(item)
    }

    override suspend fun getRecentByUser(userId: String, limit: Long): Result<List<Media>> {
        return historyStorage.getRecentByUser(userId, limit)
    }

    override suspend fun countCompletedByUser(userId: String): Result<Long> {
        return historyStorage.countCompletedByUser(userId)
    }

    override suspend fun getSetting(userId: String): Result<Option<PlayerSetting>> {
        return settingStorage.getByUser(userId)
    }

    override fun getSettingFlow(userId: String): Flow<Result<Option<PlayerSetting>>> {
        return settingStorage.getByUserFlow(userId)
    }

    override suspend fun saveSetting(item: PlayerSetting): Result<Unit> {
        return settingStorage.upsert(item)
    }

    override suspend fun resetSettings(userId: String): Result<Unit> {
        return settingStorage.deleteByUser(userId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }
}
