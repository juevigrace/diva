package com.diva.app.features.media.domain

import com.diva.app.models.media.MediaMetadata
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface MediaMetadataRepository : Repository {
    fun getMetadata(mediaId: Uuid): Flow<Result<Option<MediaMetadata>>>

    suspend fun sync(): Result<Unit>

    suspend fun save(metadata: MediaMetadata): Result<Unit>

    suspend fun delete(mediaId: Uuid): Result<Unit>
}