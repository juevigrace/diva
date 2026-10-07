package com.diva.app.collection.domain

import com.diva.app.collection.models.Collection
import com.diva.app.collection.models.CollectionMedia
import com.diva.app.media.models.Media
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow


interface CollectionRepository : Repository {
    fun observe(): Flow<Result<List<Collection>>>

    fun observe(id: String): Flow<Result<Collection>>

    suspend fun getMediaForCollection(collectionId: String): Result<List<Media>>

    suspend fun addMedia(collectionId: String, item: CollectionMedia): Result<Unit>

    suspend fun removeMedia(collectionId: String, mediaId: String): Result<Unit>

    suspend fun updateScore(collectionId: String, mediaId: String, score: Float): Result<Unit>

    suspend fun updatePosition(collectionId: String, mediaId: String, position: Int): Result<Unit>

    suspend fun countByCollection(collectionId: String): Result<Long>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(collection: Collection): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
