package com.diva.app.player.domain

import com.diva.app.media.models.Media
import com.diva.app.player.models.PlayerSetting
import com.diva.app.player.playback.models.PlaybackHistory
import com.diva.app.player.playback.models.ResumePoint
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface PlayerRepository : Repository {
    suspend fun getResumePoint(mediaId: String): Result<ResumePoint>

    suspend fun upsertResumePoint(item: ResumePoint): Result<Unit>

    suspend fun deleteResumePoint(mediaId: String): Result<Unit>

    suspend fun deleteCompletedByUser(userId: String): Result<Unit>

    suspend fun getResumable(): Result<List<Media>>

    fun observeResumable(): Flow<Result<List<Media>>>

    suspend fun recordPlayback(item: PlaybackHistory): Result<Unit>

    suspend fun getRecent(limit: Long): Result<List<Media>>

    suspend fun countCompleted(): Result<Long>

    suspend fun getSetting(): Result<PlayerSetting>

    fun observeSetting(): Flow<Result<PlayerSetting>>

    suspend fun upsertSetting(item: PlayerSetting): Result<Unit>

    suspend fun resetSettings(userId: String): Result<Unit>

    suspend fun sync(): Result<Unit>
}
