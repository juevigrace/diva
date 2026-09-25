package com.diva.app.media.database

import com.diva.app.database.media.DivaDB
import com.diva.app.models.collection.VisibilityType
import com.diva.app.models.media.Media
import com.diva.app.models.media.MediaType
import com.diva.app.models.media.tag.Tag
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow

interface MediaTagStorage {
    suspend fun getTagsForMedia(mediaId: String): Result<List<Tag>>

    fun getTagsForMediaFlow(mediaId: String): Flow<Result<List<Tag>>>

    suspend fun getMediaForTag(tagId: String): Result<List<Media>>

    suspend fun add(mediaId: String, tagId: String): Result<Unit>

    suspend fun remove(mediaId: String, tagId: String): Result<Unit>

    suspend fun removeAllByMedia(mediaId: String): Result<Unit>

    suspend fun removeAllByTag(tagId: String): Result<Unit>
}

class MediaTagStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : MediaTagStorage {

    override suspend fun getTagsForMedia(mediaId: String): Result<List<Tag>> {
        return db.getList { mediaTagQueries.findTagsForMedia(mediaId, ::mapToTag) }
    }

    override fun getTagsForMediaFlow(mediaId: String): Flow<Result<List<Tag>>> {
        return db.getListAsFlow { mediaTagQueries.findTagsForMedia(mediaId, ::mapToTag) }
    }

    override suspend fun getMediaForTag(tagId: String): Result<List<Media>> {
        return db.getList { mediaTagQueries.findMediaForTag(tagId, ::mapToMedia) }
    }

    override suspend fun add(mediaId: String, tagId: String): Result<Unit> {
        return db.use {
            transaction {
                mediaTagQueries.insert(mediaId, tagId)
            }
        }
    }

    override suspend fun remove(mediaId: String, tagId: String): Result<Unit> {
        return db.use {
            transaction {
                mediaTagQueries.deleteOne(mediaId, tagId)
            }
        }
    }

    override suspend fun removeAllByMedia(mediaId: String): Result<Unit> {
        return db.use {
            transaction {
                mediaTagQueries.deleteByMedia(mediaId)
            }
        }
    }

    override suspend fun removeAllByTag(tagId: String): Result<Unit> {
        return db.use {
            transaction {
                mediaTagQueries.deleteByTag(tagId)
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
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = Option.of(deletedAt),
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
