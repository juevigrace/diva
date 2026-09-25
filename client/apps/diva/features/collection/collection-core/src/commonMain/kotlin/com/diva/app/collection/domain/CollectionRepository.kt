package com.diva.app.collection.domain

import com.diva.app.models.collection.Collection
import com.diva.app.models.collection.CollectionMedia
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow


interface CollectionRepository : Repository {
    fun getCollections(): Flow<Result<List<Collection>>>

    fun getCollection(id: String): Flow<Result<Option<Collection>>>

    suspend fun getMediaForCollection(collectionId: String): Result<List<Media>>

    suspend fun addMedia(collectionId: String, item: CollectionMedia): Result<Unit>

    suspend fun removeMedia(collectionId: String, mediaId: String): Result<Unit>

    suspend fun updateScore(collectionId: String, mediaId: String, score: Float): Result<Unit>

    suspend fun updatePosition(collectionId: String, mediaId: String, position: Int): Result<Unit>

    suspend fun countByCollection(collectionId: String): Result<Long>

    suspend fun sync(): Result<Unit>

    suspend fun save(collection: Collection): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
