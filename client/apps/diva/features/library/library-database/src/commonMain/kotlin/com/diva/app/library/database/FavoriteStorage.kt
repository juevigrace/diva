package com.diva.app.library.database

import com.diva.app.models.collection.VisibilityType
import com.diva.app.models.media.Media
import com.diva.app.models.media.MediaType
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrElse
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

interface FavoriteStorage {
    suspend fun toggle(userId: String, mediaId: String): Result<Unit>

    suspend fun remove(userId: String, mediaId: String): Result<Unit>

    suspend fun isFavorite(userId: String, mediaId: String): Result<Boolean>

    suspend fun getFavoritesByUser(userId: String): Result<List<Media>>

    fun getFavoritesByUserFlow(userId: String): Flow<Result<List<Media>>>
}

class FavoriteStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : FavoriteStorage {

    override suspend fun toggle(userId: String, mediaId: String): Result<Unit> {
        return db.use {
            transaction {
                val count = favoriteQueries.isFavorite(userId, mediaId).executeAsOne()
                if (count > 0L) {
                    favoriteQueries.delete(userId, mediaId)
                } else {
                    favoriteQueries.insert(
                        user_id = userId,
                        media_id = mediaId,
                        created_at = Clock.System.now().epochSeconds,
                    )
                }
            }
        }
    }

    override suspend fun remove(userId: String, mediaId: String): Result<Unit> {
        return db.use {
            transaction {
                favoriteQueries.delete(userId, mediaId)
            }
        }
    }

    override suspend fun isFavorite(userId: String, mediaId: String): Result<Boolean> {
        return db.getOne { favoriteQueries.isFavorite(userId, mediaId) }
            .map { it.getOrElse { 0L } > 0L }
    }

    override suspend fun getFavoritesByUser(userId: String): Result<List<Media>> {
        return db.getList { favoriteQueries.findFavoritesByUser(userId, ::mapToMedia) }
    }

    override fun getFavoritesByUserFlow(userId: String): Flow<Result<List<Media>>> {
        return db.getListAsFlow { favoriteQueries.findFavoritesByUser(userId, ::mapToMedia) }
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
