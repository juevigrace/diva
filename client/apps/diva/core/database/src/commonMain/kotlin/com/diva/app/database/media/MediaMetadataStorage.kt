package com.diva.app.database.media

import com.diva.app.models.media.MediaMetadata
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface MediaMetadataStorage {
    suspend fun getAll(): Result<List<MediaMetadata>>

    fun getAllFlow(): Flow<Result<List<MediaMetadata>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getByMedia(mediaId: Uuid): Result<Option<MediaMetadata>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByMediaFlow(mediaId: Uuid): Flow<Result<Option<MediaMetadata>>>

    suspend fun upsert(item: MediaMetadata): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(mediaId: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
