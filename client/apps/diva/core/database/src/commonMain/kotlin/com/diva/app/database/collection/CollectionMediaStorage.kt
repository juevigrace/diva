package com.diva.app.database.collection

import com.diva.app.models.collection.CollectionMedia
import com.diva.app.models.media.Media
import kotlin.uuid.Uuid

interface CollectionMediaStorage {
    suspend fun add(collectionId: Uuid, item: CollectionMedia): Result<Unit>

    suspend fun remove(collectionId: Uuid, mediaId: Uuid): Result<Unit>

    suspend fun removeAllByCollection(collectionId: Uuid): Result<Unit>

    suspend fun updateScore(collectionId: Uuid, mediaId: Uuid, score: Float): Result<Unit>

    suspend fun updatePosition(collectionId: Uuid, mediaId: Uuid, position: Int): Result<Unit>

    suspend fun getMediaForCollection(collectionId: Uuid): Result<List<Media>>

    suspend fun countByCollection(collectionId: Uuid): Result<Long>
}
