package com.diva.app.database.playback

import com.diva.app.models.media.Media
import com.diva.app.models.playback.PlaybackHistory
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface PlaybackHistoryStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun record(item: PlaybackHistory): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getRecentByUser(userId: Uuid, limit: Long): Result<List<Media>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun countCompletedByUser(userId: Uuid): Result<Long>
}
