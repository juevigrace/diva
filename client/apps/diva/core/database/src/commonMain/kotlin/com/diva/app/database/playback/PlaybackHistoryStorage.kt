package com.diva.app.database.playback

import com.diva.app.models.media.Media
import com.diva.app.models.playback.PlaybackHistory
import kotlin.uuid.Uuid

interface PlaybackHistoryStorage {
    suspend fun record(item: PlaybackHistory): Result<Unit>

    suspend fun getRecentByUser(userId: Uuid, limit: Long): Result<List<Media>>

    suspend fun countCompletedByUser(userId: Uuid): Result<Long>
}
