package com.diva.app.features.media.domain

import com.diva.app.models.media.Media
import com.diva.app.models.media.tag.Tag
import io.github.juevigrace.diva.core.*
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface MediaRepository : Repository {
    fun getMedia(): Flow<Result<List<Media>>>

    fun getMedia(id: Uuid): Flow<Result<Option<Media>>>

    fun getTagsForMedia(mediaId: Uuid): Flow<Result<List<Tag>>>

    suspend fun getMediaForTag(tagId: Uuid): Result<List<Media>>

    suspend fun addTag(mediaId: Uuid, tagId: Uuid): Result<Unit>

    suspend fun removeTag(mediaId: Uuid, tagId: Uuid): Result<Unit>

    suspend fun sync(): Result<Unit>

    suspend fun save(media: Media): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>
}