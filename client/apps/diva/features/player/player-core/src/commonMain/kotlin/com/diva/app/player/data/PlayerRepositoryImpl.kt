package com.diva.app.player.data

import com.diva.app.media.models.Media
import com.diva.app.player.database.PlaybackHistoryStorage
import com.diva.app.player.database.PlayerSettingStorage
import com.diva.app.player.database.ResumePointStorage
import com.diva.app.player.domain.PlayerRepository
import com.diva.app.player.models.PlayerSetting
import com.diva.app.player.playback.models.PlaybackHistory
import com.diva.app.player.playback.models.ResumePoint
import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.session.domain.withSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayerRepositoryImpl(
    private val resumePointStorage: ResumePointStorage,
    private val historyStorage: PlaybackHistoryStorage,
    private val settingStorage: PlayerSettingStorage,
    private val sessionRepository: SessionRepository,
) : PlayerRepository {

    override suspend fun getResumePoint(mediaId: String): Result<ResumePoint> = withSession(sessionRepository::get) { session ->
        resumePointStorage.getByUserAndMedia(session.userId, mediaId).mapCatching { option ->
            option.getOrThrow { IllegalStateException("No resume point for media '$mediaId'") }
        }
    }

    override suspend fun upsertResumePoint(item: ResumePoint): Result<Unit> {
        return resumePointStorage.upsert(item)
    }

    override suspend fun deleteResumePoint(mediaId: String): Result<Unit> = withSession(sessionRepository::get) { session ->
        resumePointStorage.delete(session.userId, mediaId)
    }

    override suspend fun deleteCompletedByUser(userId: String): Result<Unit> {
        return resumePointStorage.deleteCompletedByUser(userId)
    }

    override suspend fun getResumable(): Result<List<Media>> = withSession(sessionRepository::get) { session ->
        resumePointStorage.getResumableByUser(session.userId)
    }

    override fun observeResumable(): Flow<Result<List<Media>>> = observeSession(sessionRepository::observe) { session ->
        resumePointStorage.getResumableByUserFlow(session.userId)
    }

    override suspend fun recordPlayback(item: PlaybackHistory): Result<Unit> {
        return historyStorage.record(item)
    }

    override suspend fun getRecent(limit: Long): Result<List<Media>> = withSession(sessionRepository::get) { session ->
        historyStorage.getRecentByUser(session.userId, limit)
    }

    override suspend fun countCompleted(): Result<Long> = withSession(sessionRepository::get) { session ->
        historyStorage.countCompletedByUser(session.userId)
    }

    override suspend fun getSetting(): Result<PlayerSetting> = withSession(sessionRepository::get) { session ->
        settingStorage.getByUser(session.userId).mapCatching { option ->
            option.getOrThrow { IllegalStateException("No player settings for user '${session.userId}'") }
        }
    }

    override fun observeSetting(): Flow<Result<PlayerSetting>> = observeSession(sessionRepository::observe) { session ->
        settingStorage.getByUserFlow(session.userId).map { result ->
            result.mapCatching { option ->
                option.getOrThrow { IllegalStateException("No player settings for user '${session.userId}'") }
            }
        }
    }

    override suspend fun upsertSetting(item: PlayerSetting): Result<Unit> {
        return settingStorage.upsert(item)
    }

    override suspend fun resetSettings(userId: String): Result<Unit> {
        return settingStorage.deleteByUser(userId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }
}
