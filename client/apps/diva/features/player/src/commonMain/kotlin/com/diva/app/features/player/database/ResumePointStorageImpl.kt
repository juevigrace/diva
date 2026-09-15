package com.diva.app.features.player.database

import com.diva.app.database.DivaDB
import com.diva.app.database.playback.ResumePointStorage
import com.diva.app.features.player.presentation.constant.MediaMapper
import com.diva.app.models.media.Media
import com.diva.app.models.playback.ResumePoint
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

class ResumePointStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : ResumePointStorage {

    override suspend fun getByUserAndMedia(userId: Uuid, mediaId: Uuid): Result<Option<ResumePoint>> {
        return db.getOne {
            resumePointQueries.findOneByMedia(userId.toString(), mediaId.toString()) { positionMs, updatedAt ->
                ResumePoint(
                    userId = userId.toString(),
                    mediaId = mediaId.toString(),
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

    override suspend fun delete(userId: Uuid, mediaId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                resumePointQueries.delete(userId.toString(), mediaId.toString())
            }
        }
    }

    override suspend fun deleteCompletedByUser(userId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                resumePointQueries.deleteCompleted(userId.toString())
            }
        }
    }

    override suspend fun getResumableByUser(userId: Uuid): Result<List<Media>> {
        return db.getList { resumePointQueries.findResumableByUser(userId.toString(), ::mapToMedia) }
    }

    override fun getResumableByUserFlow(userId: Uuid): Flow<Result<List<Media>>> {
        return db.getListAsFlow { resumePointQueries.findResumableByUser(userId.toString(), ::mapToMedia) }
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
        resumePositionMs: Long,
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