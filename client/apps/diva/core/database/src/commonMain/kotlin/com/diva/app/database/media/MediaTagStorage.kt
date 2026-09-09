package com.diva.app.database.media

import com.diva.app.models.media.Media
import com.diva.app.models.media.tag.Tag
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface MediaTagStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getTagsForMedia(mediaId: Uuid): Result<List<Tag>>

    @OptIn(ExperimentalUuidApi::class)
    fun getTagsForMediaFlow(mediaId: Uuid): Flow<Result<List<Tag>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getMediaForTag(tagId: Uuid): Result<List<Media>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun add(mediaId: Uuid, tagId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun remove(mediaId: Uuid, tagId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun removeAllByMedia(mediaId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun removeAllByTag(tagId: Uuid): Result<Unit>
}
