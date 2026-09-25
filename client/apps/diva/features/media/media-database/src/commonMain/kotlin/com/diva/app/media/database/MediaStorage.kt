package com.diva.app.media.database

import com.diva.app.database.media.DivaDB
import com.diva.app.models.collection.VisibilityType
import com.diva.app.models.media.Media
import com.diva.app.models.media.MediaType
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow
import migrations.Diva_media

interface MediaStorage {
    suspend fun getAll(): Result<List<Media>>

    fun getAllFlow(): Flow<Result<List<Media>>>

    suspend fun getById(id: String): Result<Option<Media>>

    fun getByIdFlow(id: String): Flow<Result<Option<Media>>>

    suspend fun upsert(item: Media): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}

class MediaStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : MediaStorage {

    override suspend fun getAll(): Result<List<Media>> {
        return db.getList { mediaQueries.findAll(::mapToMedia) }
    }

    override fun getAllFlow(): Flow<Result<List<Media>>> {
        return db.getListAsFlow { mediaQueries.findAll(::mapToMedia) }
    }

    override suspend fun getById(id: String): Result<Option<Media>> {
        return db.getOne { mediaQueries.findOneById(id, ::mapToMedia) }
    }

    override fun getByIdFlow(id: String): Flow<Result<Option<Media>>> {
        return db.getOneAsFlow { mediaQueries.findOneById(id, ::mapToMedia) }
    }

    override suspend fun upsert(item: Media): Result<Unit> {
        return db.use {
            transaction {
                mediaQueries.upsert(
                    Diva_media(
                        id = item.id,
                        submitted_by = item.submittedBy.id,
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
                        published_at = item.publishedAt,
                        fingerprint = item.fingerprint.getOrNull(),
                        created_at = item.createdAt,
                        updated_at = item.updatedAt,
                        deleted_at = item.deletedAt.getOrNull(),
                    )
                )
            }
        }
    }

    override suspend fun delete(id: String): Result<Unit> {
        return db.use {
            transaction {
                mediaQueries.deleteById(id)
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
