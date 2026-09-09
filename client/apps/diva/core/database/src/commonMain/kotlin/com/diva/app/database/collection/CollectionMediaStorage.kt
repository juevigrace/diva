package com.diva.app.database.collection

import com.diva.app.models.collection.CollectionMedia
import com.diva.app.models.media.Media
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface CollectionMediaStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun add(collectionId: Uuid, item: CollectionMedia): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun remove(collectionId: Uuid, mediaId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun removeAllByCollection(collectionId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun updateScore(collectionId: Uuid, mediaId: Uuid, score: Float): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun updatePosition(collectionId: Uuid, mediaId: Uuid, position: Int): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getMediaForCollection(collectionId: Uuid): Result<List<Media>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun countByCollection(collectionId: Uuid): Result<Long>
}
