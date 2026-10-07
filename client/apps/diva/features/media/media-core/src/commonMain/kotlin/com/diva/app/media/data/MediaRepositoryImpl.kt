package com.diva.app.media.data

import com.diva.app.media.database.MediaStorage
import com.diva.app.media.database.MediaTagStorage
import com.diva.app.media.domain.MediaRepository
import com.diva.app.media.models.Media
import com.diva.app.media.tag.models.Tag
import io.github.juevigrace.diva.core.getOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaRepositoryImpl(
    private val storage: MediaStorage,
    private val mediaTagStorage: MediaTagStorage,
) : MediaRepository {

    override fun observe(): Flow<Result<List<Media>>> = storage.getAllFlow()

    override fun observe(id: String): Flow<Result<Media>> = storage.getByIdFlow(id).map { result ->
        result.mapCatching { option ->
            option.getOrThrow { IllegalStateException("No media '$id'") }
        }
    }

    override fun getTagsForMedia(mediaId: String): Flow<Result<List<Tag>>> {
        return mediaTagStorage.getTagsForMediaFlow(mediaId)
    }

    override suspend fun getMediaForTag(tagId: String): Result<List<Media>> {
        return mediaTagStorage.getMediaForTag(tagId)
    }

    override suspend fun addTag(mediaId: String, tagId: String): Result<Unit> {
        return mediaTagStorage.add(mediaId, tagId)
    }

    override suspend fun removeTag(mediaId: String, tagId: String): Result<Unit> {
        return mediaTagStorage.remove(mediaId, tagId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun upsert(media: Media): Result<Unit> = storage.upsert(media)

    override suspend fun delete(id: String): Result<Unit> = storage.delete(id)
}
