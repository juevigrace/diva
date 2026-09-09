package com.diva.app.features.media.database

import com.diva.app.database.DivaDB
import com.diva.app.database.media.MediaStorage
import com.diva.app.models.collection.VisibilityType
import com.diva.app.models.media.Media
import com.diva.app.models.media.MediaType
import io.github.juevigrace.diva.core.*
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import migrations.Diva_media

@OptIn(ExperimentalUuidApi::class)
class MediaStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : MediaStorage {

    override suspend fun getAll(): Result<List<Media>> {
        return db.getList { mediaQueries.findAll(::mapToMedia) }
    }

    override fun getAllFlow(): Flow<Result<List<Media>>> {
        return db.getListAsFlow { mediaQueries.findAll(::mapToMedia) }
    }

    override suspend fun getById(id: Uuid): Result<Option<Media>> {
        return db.getOne { mediaQueries.findOneById(id.toString(), ::mapToMedia) }
    }

    override fun getByIdFlow(id: Uuid): Flow<Result<Option<Media>>> {
        return db.getOneAsFlow { mediaQueries.findOneById(id.toString(), ::mapToMedia) }
    }

    override suspend fun upsert(item: Media): Result<Unit> {
        return db.use {
            transaction {
                mediaQueries.upsert(
                    Diva_media(
                        id = item.id.toString(),
                        submitted_by = item.submittedBy.id.toString(),
                        media_type = item.mediaType,
                        title = item.title,
                        uri = item.uri,
                        mime_type = item.mimeType,
                        size_bytes = item.sizeBytes,
                        duration_ms = item.durationMs.getOrNull(),
                        width = item.width.toLong(),
                        height = item.height.toLong(),
                        alt_text = item.altText,
                        visibility = item.visibility,
                        sensitive_content = item.sensitiveContent,
                        adult_content = item.adultContent,
                        published_at = item.publishedAt.epochSeconds,
                        fingerprint = item.fingerprint.getOrNull(),
                        created_at = item.createdAt.epochSeconds,
                        updated_at = item.updatedAt.epochSeconds,
                        deleted_at = item.deletedAt.map { it.epochSeconds }.getOrNull(),
                    )
                )
            }
        }
    }

    override suspend fun delete(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                mediaQueries.deleteById(id.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                mediaQueries.deleteAll()
            }
        }
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
        id = Uuid.parse(id),
        submittedBy = User(id = Uuid.parse(submittedBy)),
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
        publishedAt = Instant.fromEpochSeconds(publishedAt),
        fingerprint = Option.of(fingerprint),
        createdAt = Instant.fromEpochSeconds(createdAt),
        updatedAt = Instant.fromEpochSeconds(updatedAt),
        deletedAt = Option.of(deletedAt?.let { Instant.fromEpochSeconds(it) }),
    )
}