package com.diva.app.media.data

import com.diva.app.media.database.MediaMetadataStorage
import com.diva.app.media.domain.MediaMetadataRepository
import com.diva.app.media.models.MediaMetadata
import io.github.juevigrace.diva.core.getOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaMetadataRepositoryImpl(
    private val storage: MediaMetadataStorage,
) : MediaMetadataRepository {

    override fun observe(mediaId: String): Flow<Result<MediaMetadata>> = storage.getByMediaFlow(mediaId).map { result ->
        result.mapCatching { option ->
            option.getOrThrow { IllegalStateException("No metadata for media '$mediaId'") }
        }
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun upsert(metadata: MediaMetadata): Result<Unit> = storage.upsert(metadata)

    override suspend fun delete(mediaId: String): Result<Unit> = storage.delete(mediaId)
}
