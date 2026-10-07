package com.diva.app.media.domain

import com.diva.app.media.models.MediaMetadata
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface MediaMetadataRepository : Repository {
    fun observe(mediaId: String): Flow<Result<MediaMetadata>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(metadata: MediaMetadata): Result<Unit>

    suspend fun delete(mediaId: String): Result<Unit>
}
