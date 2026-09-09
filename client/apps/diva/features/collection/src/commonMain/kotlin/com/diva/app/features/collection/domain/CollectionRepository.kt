package com.diva.app.features.collection.domain

import com.diva.app.models.collection.Collection
import com.diva.app.models.collection.CollectionMedia
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.*
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface CollectionRepository : Repository {
    fun getCollections(): Flow<Result<List<Collection>>>

    fun getCollection(id: Uuid): Flow<Result<Option<Collection>>>

    suspend fun getMediaForCollection(collectionId: Uuid): Result<List<Media>>

    suspend fun addMedia(collectionId: Uuid, item: CollectionMedia): Result<Unit>

    suspend fun removeMedia(collectionId: Uuid, mediaId: Uuid): Result<Unit>

    suspend fun updateScore(collectionId: Uuid, mediaId: Uuid, score: Float): Result<Unit>

    suspend fun updatePosition(collectionId: Uuid, mediaId: Uuid, position: Int): Result<Unit>

    suspend fun countByCollection(collectionId: Uuid): Result<Long>

    suspend fun sync(): Result<Unit>

    suspend fun save(collection: Collection): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>
}