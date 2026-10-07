package com.diva.app.library.domain

import com.diva.app.media.models.Media
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface LibraryRepository : Repository {
    fun observe(): Flow<Result<List<Media>>>

    suspend fun toggleFavorite(mediaId: String): Result<Unit>

    suspend fun removeFavorite(mediaId: String): Result<Unit>

    suspend fun isFavorite(mediaId: String): Result<Boolean>
}
