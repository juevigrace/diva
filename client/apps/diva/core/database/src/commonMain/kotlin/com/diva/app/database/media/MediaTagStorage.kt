package com.diva.app.database.media

import com.diva.app.models.media.Media
import com.diva.app.models.media.tag.Tag
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface MediaTagStorage {
    suspend fun getTagsForMedia(mediaId: Uuid): Result<List<Tag>>

    fun getTagsForMediaFlow(mediaId: Uuid): Flow<Result<List<Tag>>>

    suspend fun getMediaForTag(tagId: Uuid): Result<List<Media>>

    suspend fun add(mediaId: Uuid, tagId: Uuid): Result<Unit>

    suspend fun remove(mediaId: Uuid, tagId: Uuid): Result<Unit>

    suspend fun removeAllByMedia(mediaId: Uuid): Result<Unit>

    suspend fun removeAllByTag(tagId: Uuid): Result<Unit>
}
