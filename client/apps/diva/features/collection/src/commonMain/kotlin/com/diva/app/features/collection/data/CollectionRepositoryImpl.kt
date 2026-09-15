package com.diva.app.features.collection.data

import com.diva.app.database.collection.CollectionMediaStorage
import com.diva.app.database.collection.CollectionStorage
import com.diva.app.features.collection.domain.CollectionRepository
import com.diva.app.models.collection.Collection
import com.diva.app.models.collection.CollectionMedia
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

class CollectionRepositoryImpl(
    private val storage: CollectionStorage,
    private val mediaStorage: CollectionMediaStorage,
) : CollectionRepository {

    override fun getCollections(): Flow<Result<List<Collection>>> = storage.getAllFlow()

    override fun getCollection(id: Uuid): Flow<Result<Option<Collection>>> {
        return storage.getByIdFlow(id)
    }

    override suspend fun getMediaForCollection(collectionId: Uuid): Result<List<Media>> {
        return mediaStorage.getMediaForCollection(collectionId)
    }

    override suspend fun addMedia(collectionId: Uuid, item: CollectionMedia): Result<Unit> {
        return mediaStorage.add(collectionId, item)
    }

    override suspend fun removeMedia(collectionId: Uuid, mediaId: Uuid): Result<Unit> {
        return mediaStorage.remove(collectionId, mediaId)
    }

    override suspend fun updateScore(collectionId: Uuid, mediaId: Uuid, score: Float): Result<Unit> {
        return mediaStorage.updateScore(collectionId, mediaId, score)
    }

    override suspend fun updatePosition(collectionId: Uuid, mediaId: Uuid, position: Int): Result<Unit> {
        return mediaStorage.updatePosition(collectionId, mediaId, position)
    }

    override suspend fun countByCollection(collectionId: Uuid): Result<Long> {
        return mediaStorage.countByCollection(collectionId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(collection: Collection): Result<Unit> = storage.upsert(collection)

    override suspend fun delete(id: Uuid): Result<Unit> = storage.delete(id)
}