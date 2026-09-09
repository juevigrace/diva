package com.diva.app.database.playback

import com.diva.app.models.media.Media
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface FavoriteStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun toggle(userId: Uuid, mediaId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun remove(userId: Uuid, mediaId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun isFavorite(userId: Uuid, mediaId: Uuid): Result<Boolean>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getFavoritesByUser(userId: Uuid): Result<List<Media>>

    @OptIn(ExperimentalUuidApi::class)
    fun getFavoritesByUserFlow(userId: Uuid): Flow<Result<List<Media>>>
}
