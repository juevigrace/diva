package com.diva.app.library.domain

import com.diva.app.folder.models.Folder
import com.diva.app.media.models.Media
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
