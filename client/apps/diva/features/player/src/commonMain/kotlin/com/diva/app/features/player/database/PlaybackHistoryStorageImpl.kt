package com.diva.app.features.player.database

import com.diva.app.database.DivaDB
import com.diva.app.database.playback.PlaybackHistoryStorage
import com.diva.app.features.player.presentation.constant.MediaMapper
import com.diva.app.models.media.Media
import com.diva.app.models.playback.PlaybackHistory
import io.github.juevigrace.diva.database.DivaDatabase
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class PlaybackHistoryStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : PlaybackHistoryStorage {

    override suspend fun record(item: PlaybackHistory): Result<Unit> {
        return db.use {
            transaction {
                playbackHistoryQueries.insert(
                    id = item.id.toString(),
                    user_id = item.userId.toString(),
                    media_id = item.mediaId.toString(),
                    played_at = item.playedAt.epochSeconds,
                    position_ms = item.positionMs,
                    completed = item.completed,
                )
            }
        }
    }

    override suspend fun getRecentByUser(userId: Uuid, limit: Long): Result<List<Media>> {
        return db.getList {
            playbackHistoryQueries.findRecentByUser(userId.toString(), limit, ::mapToMedia)
        }
    }

    override suspend fun countCompletedByUser(userId: Uuid): Result<Long> {
        return db.getOne { playbackHistoryQueries.countCompletedByUser(userId.toString()) }
            .map { it.getOrElse { 0L } }
    }

    private fun mapToMedia(
        id: String,
        submittedBy: String,
        mediaType: com.diva.app.models.media.MediaType,
        title: String,
        uri: String,
        mimeType: String,
        sizeBytes: Long,
        durationMs: Long?,
        width: Long,
        height: Long,
        altText: String,
        visibility: com.diva.app.models.collection.VisibilityType,
        sensitiveContent: Boolean,
        adultContent: Boolean,
        publishedAt: Long,
        fingerprint: String?,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Media = MediaMapper.map(
        id = id,
        submittedBy = submittedBy,
        mediaType = mediaType,
        title = title,
        uri = uri,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        durationMs = durationMs,
        width = width,
        height = height,
        altText = altText,
        visibility = visibility,
        sensitiveContent = sensitiveContent,
        adultContent = adultContent,
        publishedAt = publishedAt,
        fingerprint = fingerprint,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
    )
}