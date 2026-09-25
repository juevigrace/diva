package com.diva.app.features.media.data

import com.diva.app.features.media.database.MediaMetadataStorage
import com.diva.app.features.media.domain.MediaMetadataRepository
import com.diva.app.models.media.MediaMetadata
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow

class MediaMetadataRepositoryImpl(
    private val storage: MediaMetadataStorage,
) : MediaMetadataRepository {

    override fun getMetadata(mediaId: String): Flow<Result<Option<MediaMetadata>>> {
        return storage.getByMediaFlow(mediaId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(metadata: MediaMetadata): Result<Unit> = storage.upsert(metadata)

    override suspend fun delete(mediaId: String): Result<Unit> = storage.delete(mediaId)
}
