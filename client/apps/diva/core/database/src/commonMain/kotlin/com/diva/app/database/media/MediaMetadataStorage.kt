package com.diva.app.database.media

import com.diva.app.models.media.MediaMetadata
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface MediaMetadataStorage {
    suspend fun getAll(): Result<List<MediaMetadata>>

    fun getAllFlow(): Flow<Result<List<MediaMetadata>>>

    suspend fun getByMedia(mediaId: Uuid): Result<Option<MediaMetadata>>

    fun getByMediaFlow(mediaId: Uuid): Flow<Result<Option<MediaMetadata>>>

    suspend fun upsert(item: MediaMetadata): Result<Unit>

    suspend fun delete(mediaId: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
