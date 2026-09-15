package com.diva.app.features.library.database

import com.diva.app.database.DivaDB
import com.diva.app.database.playback.FavoriteStorage
import com.diva.app.features.player.presentation.constant.MediaMapper
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.getOrElse
import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.uuid.Uuid

class FavoriteStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : FavoriteStorage {

    override suspend fun toggle(userId: Uuid, mediaId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                val count = favoriteQueries.isFavorite(userId.toString(), mediaId.toString()).executeAsOne()
                if (count > 0L) {
                    remove(userId, mediaId).getOrThrow()
                } else {
                    favoriteQueries.insert(
                        user_id = userId.toString(),
                        media_id = mediaId.toString(),
                        created_at = Clock.System.now().epochSeconds,
                    )
                }
            }
        }
    }

    override suspend fun remove(userId: Uuid, mediaId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                favoriteQueries.delete(userId.toString(), mediaId.toString())
            }
        }
    }

    override suspend fun isFavorite(userId: Uuid, mediaId: Uuid): Result<Boolean> {
        return db.getOne { favoriteQueries.isFavorite(userId.toString(), mediaId.toString()) }
            .map { it.getOrElse { 0L } > 0L }
    }

    override suspend fun getFavoritesByUser(userId: Uuid): Result<List<Media>> {
        return db.getList { favoriteQueries.findFavoritesByUser(userId.toString(), ::mapToMedia) }
    }

    override fun getFavoritesByUserFlow(userId: Uuid): Flow<Result<List<Media>>> {
        return db.getListAsFlow { favoriteQueries.findFavoritesByUser(userId.toString(), ::mapToMedia) }
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
