package com.diva.app.collection.data

import com.diva.app.collection.database.CollectionMediaStorage
import com.diva.app.collection.database.CollectionStorage
import com.diva.app.collection.domain.CollectionRepository
import com.diva.app.collection.models.Collection
import com.diva.app.collection.models.CollectionMedia
import com.diva.app.media.models.Media
import io.github.juevigrace.diva.core.getOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CollectionRepositoryImpl(
    private val storage: CollectionStorage,
    private val mediaStorage: CollectionMediaStorage,
) : CollectionRepository {

    override fun observe(): Flow<Result<List<Collection>>> = storage.getAllFlow()

    override fun observe(id: String): Flow<Result<Collection>> = storage.getByIdFlow(id).map { result ->
        result.mapCatching { option ->
            option.getOrThrow { IllegalStateException("No collection '$id'") }
        }
    }

    override suspend fun getMediaForCollection(collectionId: String): Result<List<Media>> {
        return mediaStorage.getMediaForCollection(collectionId)
    }

    override suspend fun addMedia(collectionId: String, item: CollectionMedia): Result<Unit> {
        return mediaStorage.add(collectionId, item)
    }

    override suspend fun removeMedia(collectionId: String, mediaId: String): Result<Unit> {
        return mediaStorage.remove(collectionId, mediaId)
    }

    override suspend fun updateScore(collectionId: String, mediaId: String, score: Float): Result<Unit> {
        return mediaStorage.updateScore(collectionId, mediaId, score)
    }

    override suspend fun updatePosition(collectionId: String, mediaId: String, position: Int): Result<Unit> {
        return mediaStorage.updatePosition(collectionId, mediaId, position)
    }

    override suspend fun countByCollection(collectionId: String): Result<Long> {
        return mediaStorage.countByCollection(collectionId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun upsert(collection: Collection): Result<Unit> = storage.upsert(collection)

    override suspend fun delete(id: String): Result<Unit> = storage.delete(id)
}
