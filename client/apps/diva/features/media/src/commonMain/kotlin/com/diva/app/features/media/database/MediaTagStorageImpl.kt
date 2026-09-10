package com.diva.app.features.media.database

import com.diva.app.database.DivaDB
import com.diva.app.database.media.MediaTagStorage
import com.diva.app.models.collection.VisibilityType
import com.diva.app.models.media.Media
import com.diva.app.models.media.MediaType
import com.diva.app.models.media.tag.Tag
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MediaTagStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : MediaTagStorage {

    override suspend fun getTagsForMedia(mediaId: Uuid): Result<List<Tag>> {
        return db.getList { mediaTagQueries.findTagsForMedia(mediaId.toString(), ::mapToTag) }
    }

    override fun getTagsForMediaFlow(mediaId: Uuid): Flow<Result<List<Tag>>> {
        return db.getListAsFlow { mediaTagQueries.findTagsForMedia(mediaId.toString(), ::mapToTag) }
    }

    override suspend fun getMediaForTag(tagId: Uuid): Result<List<Media>> {
        return db.getList { mediaTagQueries.findMediaForTag(tagId.toString(), ::mapToMedia) }
    }

    override suspend fun add(mediaId: Uuid, tagId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                mediaTagQueries.insert(mediaId.toString(), tagId.toString())
            }
        }
    }

    override suspend fun remove(mediaId: Uuid, tagId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                mediaTagQueries.deleteOne(mediaId.toString(), tagId.toString())
            }
        }
    }

    override suspend fun removeAllByMedia(mediaId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                mediaTagQueries.deleteByMedia(mediaId.toString())
            }
        }
    }

    override suspend fun removeAllByTag(tagId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                mediaTagQueries.deleteByTag(tagId.toString())
            }
        }
    }

    private fun mapToTag(
        id: String,
        name: String,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Tag = Tag(
        id = Uuid.parse(id),
        name = name,
        createdAt = Instant.fromEpochSeconds(createdAt),
        updatedAt = Instant.fromEpochSeconds(updatedAt),
        deletedAt = Option.of(deletedAt?.let { Instant.fromEpochSeconds(it) }),
    )

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