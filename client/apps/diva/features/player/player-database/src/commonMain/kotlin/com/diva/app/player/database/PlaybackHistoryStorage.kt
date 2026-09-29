package com.diva.app.player.database

import com.diva.app.core.models.VisibilityType
import com.diva.app.media.models.Media
import com.diva.app.media.models.MediaType
import com.diva.app.player.playback.models.PlaybackHistory
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrElse
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.user.models.User

interface PlaybackHistoryStorage {
    suspend fun record(item: PlaybackHistory): Result<Unit>
    suspend fun getRecentByUser(userId: String, limit: Long): Result<List<Media>>
    suspend fun countCompletedByUser(userId: String): Result<Long>
}

class PlaybackHistoryStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : PlaybackHistoryStorage {

    override suspend fun record(item: PlaybackHistory): Result<Unit> {
        return db.use {
            transaction {
                playbackHistoryQueries.insert(
                    id = item.id,
                    user_id = item.user.id,
                    media_id = item.media.id,
                    played_at = item.playedAt,
                    position_ms = item.positionMs,
                    completed = item.completed,
                )
            }
        }
    }

    override suspend fun getRecentByUser(userId: String, limit: Long): Result<List<Media>> {
        return db.getList {
            playbackHistoryQueries.findRecentByUser(userId, limit, ::mapToMedia)
        }
    }

    override suspend fun countCompletedByUser(userId: String): Result<Long> {
        return db.getOne { playbackHistoryQueries.countCompletedByUser(userId) }
            .map { it.getOrElse { 0L } }
    }

    private fun mapToMedia(
        id: String,
        submittedBy: String,
        mediaType: MediaType,
        title: String,
        uri: String,
        mimeType: String,
        sizeBytes: Long,
        durationMs: Long?,
        width: Long,
        height: Long,
        altText: String,
        visibility: VisibilityType,
        sensitiveContent: Boolean,
        adultContent: Boolean,
        publishedAt: Long,
        fingerprint: String?,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Media = Media(
        id = id,
        submittedBy = User(id = submittedBy),
        mediaType = mediaType,
        title = title,
        uri = uri,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        durationMs = Option.of(durationMs),
        width = width.toInt(),
        height = height.toInt(),
        altText = altText,
        visibility = visibility,
        sensitiveContent = sensitiveContent,
        adultContent = adultContent,
        publishedAt = publishedAt,
        fingerprint = Option.of(fingerprint),
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = Option.of(deletedAt),
    )
}
