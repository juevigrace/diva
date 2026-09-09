package com.diva.app.features.collection.database

import com.diva.app.database.DivaDB
import com.diva.app.database.collection.CollectionMediaStorage
import com.diva.app.models.collection.CollectionMedia
import com.diva.app.models.collection.VisibilityType
import com.diva.app.models.media.Media
import com.diva.app.models.media.MediaType
import io.github.juevigrace.diva.core.*
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import migrations.Diva_collection_media

@OptIn(ExperimentalUuidApi::class)
class CollectionMediaStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : CollectionMediaStorage {

    override suspend fun add(collectionId: Uuid, item: CollectionMedia): Result<Unit> {
        return db.use {
            transaction {
                collectionMediaQueries.insert(
                    Diva_collection_media(
                        collection_id = collectionId.toString(),
                        media_id = item.media.id.toString(),
                        position = item.position.toLong(),
                        added_by = item.addedBy.id.toString(),
                        score = item.score.toDouble(),
                        added_at = item.addedAt.epochSeconds,
                    )
                )
            }
        }
    }

    override suspend fun remove(collectionId: Uuid, mediaId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                collectionMediaQueries.deleteOne(collectionId.toString(), mediaId.toString())
            }
        }
    }

    override suspend fun removeAllByCollection(collectionId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                collectionMediaQueries.deleteByCollection(collectionId.toString())
            }
        }
    }

    override suspend fun updateScore(collectionId: Uuid, mediaId: Uuid, score: Float): Result<Unit> {
        return db.use {
            transaction {
                collectionMediaQueries.updateScore(
                    score = score.toDouble(),
                    collection_id = collectionId.toString(),
                    media_id = mediaId.toString(),
                )
            }
        }
    }

    override suspend fun updatePosition(collectionId: Uuid, mediaId: Uuid, position: Int): Result<Unit> {
        return db.use {
            transaction {
                collectionMediaQueries.updatePosition(
                    position = position.toLong(),
                    collection_id = collectionId.toString(),
                    media_id = mediaId.toString(),
                )
            }
        }
    }

    override suspend fun getMediaForCollection(collectionId: Uuid): Result<List<Media>> {
        return db.getList { collectionMediaQueries.findForCollection(collectionId.toString(), ::mapToMedia) }
    }

    override suspend fun countByCollection(collectionId: Uuid): Result<Long> {
        return db.getOne { collectionMediaQueries.count(collectionId.toString()) }
            .fold(
                onSuccess = { option -> Result.success(option.getOrElse { 0L }) },
                onFailure = { Result.failure(it) },
            )
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