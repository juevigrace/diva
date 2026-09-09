package com.diva.app.features.media.data

import com.diva.app.database.media.MediaMetadataStorage
import com.diva.app.features.media.domain.MediaMetadataRepository
import com.diva.app.models.media.MediaMetadata
import io.github.juevigrace.diva.core.*
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MediaMetadataRepositoryImpl(
    private val storage: MediaMetadataStorage,
) : MediaMetadataRepository {

    override fun getMetadata(mediaId: Uuid): Flow<Result<Option<MediaMetadata>>> {
        return storage.getByMediaFlow(mediaId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(metadata: MediaMetadata): Result<Unit> = storage.upsert(metadata)

    override suspend fun delete(mediaId: Uuid): Result<Unit> = storage.delete(mediaId)
}