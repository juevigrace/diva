package com.diva.app.features.media.data

import com.diva.app.database.media.MediaStorage
import com.diva.app.database.media.MediaTagStorage
import com.diva.app.features.media.domain.MediaRepository
import com.diva.app.models.media.Media
import com.diva.app.models.media.tag.Tag
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MediaRepositoryImpl(
    private val storage: MediaStorage,
    private val mediaTagStorage: MediaTagStorage,
) : MediaRepository {

    override fun getMedia(): Flow<Result<List<Media>>> = storage.getAllFlow()

    override fun getMedia(id: Uuid): Flow<Result<Option<Media>>> = storage.getByIdFlow(id)

    override fun getTagsForMedia(mediaId: Uuid): Flow<Result<List<Tag>>> {
        return mediaTagStorage.getTagsForMediaFlow(mediaId)
    }

    override suspend fun getMediaForTag(tagId: Uuid): Result<List<Media>> {
        return mediaTagStorage.getMediaForTag(tagId)
    }

    override suspend fun addTag(mediaId: Uuid, tagId: Uuid): Result<Unit> {
        return mediaTagStorage.add(mediaId, tagId)
    }

    override suspend fun removeTag(mediaId: Uuid, tagId: Uuid): Result<Unit> {
        return mediaTagStorage.remove(mediaId, tagId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(media: Media): Result<Unit> = storage.upsert(media)

    override suspend fun delete(id: Uuid): Result<Unit> = storage.delete(id)
}