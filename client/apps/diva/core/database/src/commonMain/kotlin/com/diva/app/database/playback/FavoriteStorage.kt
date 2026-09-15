package com.diva.app.database.playback

import com.diva.app.models.media.Media
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface FavoriteStorage {
    suspend fun toggle(userId: Uuid, mediaId: Uuid): Result<Unit>

    suspend fun remove(userId: Uuid, mediaId: Uuid): Result<Unit>

    suspend fun isFavorite(userId: Uuid, mediaId: Uuid): Result<Boolean>

    suspend fun getFavoritesByUser(userId: Uuid): Result<List<Media>>

    fun getFavoritesByUserFlow(userId: Uuid): Flow<Result<List<Media>>>
}
