package com.diva.app.features.library.domain

import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface LibraryRepository : Repository {
    fun getFavorites(userId: Uuid): Flow<Result<List<Media>>>

    suspend fun toggleFavorite(userId: Uuid, mediaId: Uuid): Result<Unit>

    suspend fun removeFavorite(userId: Uuid, mediaId: Uuid): Result<Unit>

    suspend fun isFavorite(userId: Uuid, mediaId: Uuid): Result<Boolean>

    suspend fun getRecent(userId: Uuid, limit: Long): Result<List<Media>>

    suspend fun getResumable(userId: Uuid): Result<List<Media>>

    suspend fun getFolders(userId: Uuid): Result<List<Folder>>

    suspend fun sync(): Result<Unit>
}