package com.diva.app.features.player.domain

import com.diva.app.models.media.Media
import com.diva.app.models.player.PlayerSetting
import com.diva.app.models.playback.PlaybackHistory
import com.diva.app.models.playback.ResumePoint
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface PlayerRepository : Repository {
    suspend fun getResumePoint(userId: Uuid, mediaId: Uuid): Result<Option<ResumePoint>>

    suspend fun saveResumePoint(item: ResumePoint): Result<Unit>

    suspend fun deleteResumePoint(userId: Uuid, mediaId: Uuid): Result<Unit>

    suspend fun deleteCompletedByUser(userId: Uuid): Result<Unit>

    suspend fun getResumableByUser(userId: Uuid): Result<List<Media>>

    fun getResumableByUserFlow(userId: Uuid): Flow<Result<List<Media>>>

    suspend fun recordPlayback(item: PlaybackHistory): Result<Unit>

    suspend fun getRecentByUser(userId: Uuid, limit: Long): Result<List<Media>>

    suspend fun countCompletedByUser(userId: Uuid): Result<Long>

    suspend fun getSetting(userId: Uuid): Result<Option<PlayerSetting>>

    fun getSettingFlow(userId: Uuid): Flow<Result<Option<PlayerSetting>>>

    suspend fun saveSetting(item: PlayerSetting): Result<Unit>

    suspend fun resetSettings(userId: Uuid): Result<Unit>

    suspend fun sync(): Result<Unit>
}