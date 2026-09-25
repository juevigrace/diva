package com.diva.app.player.database

import com.diva.app.database.player.DivaDB
import com.diva.app.models.collection.VisibilityType
import com.diva.app.models.media.Media
import com.diva.app.models.media.MediaType
import com.diva.app.models.playback.ResumePoint
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow

interface ResumePointStorage {
    suspend fun getByUserAndMedia(userId: String, mediaId: String): Result<Option<ResumePoint>>

    suspend fun upsert(item: ResumePoint): Result<Unit>

    suspend fun delete(userId: String, mediaId: String): Result<Unit>

    suspend fun deleteCompletedByUser(userId: String): Result<Unit>

    suspend fun getResumableByUser(userId: String): Result<List<Media>>

    fun getResumableByUserFlow(userId: String): Flow<Result<List<Media>>>
}

class ResumePointStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : ResumePointStorage {

    override suspend fun getByUserAndMedia(userId: String, mediaId: String): Result<Option<ResumePoint>> {
        return db.getOne {
            resumePointQueries.findOneByMedia(userId, mediaId) { positionMs, updatedAt ->
                ResumePoint(
                    userId = userId,
                    mediaId = mediaId,
                    positionMs = positionMs,
                    updatedAt = updatedAt,
                )
            }
        }
    }

    override suspend fun upsert(item: ResumePoint): Result<Unit> {
        return db.use {
            transaction {
                resumePointQueries.upsert(
                    user_id = item.userId,
                    media_id = item.mediaId,
                    position_ms = item.positionMs,
                    updated_at = item.updatedAt,
                )
            }
        }
    }

    override suspend fun delete(userId: String, mediaId: String): Result<Unit> {
        return db.use {
            transaction {
                resumePointQueries.delete(userId, mediaId)
            }
        }
    }

    override suspend fun deleteCompletedByUser(userId: String): Result<Unit> {
        return db.use {
            transaction {
                resumePointQueries.deleteCompleted(userId)
            }
        }
    }

    override suspend fun getResumableByUser(userId: String): Result<List<Media>> {
        return db.getList { resumePointQueries.findResumableByUser(userId, ::mapToMedia) }
    }

    override fun getResumableByUserFlow(userId: String): Flow<Result<List<Media>>> {
        return db.getListAsFlow { resumePointQueries.findResumableByUser(userId, ::mapToMedia) }
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
        resumePositionMs: Long,
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
