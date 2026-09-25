package com.diva.app.player.domain

import com.diva.app.models.media.Media
import com.diva.app.models.playback.PlaybackHistory
import com.diva.app.models.playback.ResumePoint
import com.diva.app.models.player.PlayerSetting
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface PlayerRepository : Repository {
    suspend fun getResumePoint(userId: String, mediaId: String): Result<Option<ResumePoint>>

    suspend fun saveResumePoint(item: ResumePoint): Result<Unit>

    suspend fun deleteResumePoint(userId: String, mediaId: String): Result<Unit>

    suspend fun deleteCompletedByUser(userId: String): Result<Unit>

    suspend fun getResumableByUser(userId: String): Result<List<Media>>

    fun getResumableByUserFlow(userId: String): Flow<Result<List<Media>>>

    suspend fun recordPlayback(item: PlaybackHistory): Result<Unit>

    suspend fun getRecentByUser(userId: String, limit: Long): Result<List<Media>>

    suspend fun countCompletedByUser(userId: String): Result<Long>

    suspend fun getSetting(userId: String): Result<Option<PlayerSetting>>

    fun getSettingFlow(userId: String): Flow<Result<Option<PlayerSetting>>>

    suspend fun saveSetting(item: PlayerSetting): Result<Unit>

    suspend fun resetSettings(userId: String): Result<Unit>

    suspend fun sync(): Result<Unit>
}
