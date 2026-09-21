package com.diva.app.features.library.domain

import com.diva.app.models.folder.Folder
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface LibraryRepository : Repository {
    fun getFavorites(userId: String): Flow<Result<List<Media>>>

    suspend fun toggleFavorite(userId: String, mediaId: String): Result<Unit>

    suspend fun removeFavorite(userId: String, mediaId: String): Result<Unit>

    suspend fun isFavorite(userId: String, mediaId: String): Result<Boolean>

    suspend fun getRecent(userId: String, limit: Long): Result<List<Media>>

    suspend fun getResumable(userId: String): Result<List<Media>>

    suspend fun getFolders(userId: String): Result<List<Folder>>

    suspend fun sync(): Result<Unit>
}
