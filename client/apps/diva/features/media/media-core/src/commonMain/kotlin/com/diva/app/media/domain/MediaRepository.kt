package com.diva.app.media.domain

import com.diva.app.media.models.Media
import com.diva.app.media.tag.models.Tag
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface MediaRepository : Repository {
    fun observe(): Flow<Result<List<Media>>>

    fun observe(id: String): Flow<Result<Media>>

    fun getTagsForMedia(mediaId: String): Flow<Result<List<Tag>>>

    suspend fun getMediaForTag(tagId: String): Result<List<Media>>

    suspend fun addTag(mediaId: String, tagId: String): Result<Unit>

    suspend fun removeTag(mediaId: String, tagId: String): Result<Unit>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(media: Media): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
