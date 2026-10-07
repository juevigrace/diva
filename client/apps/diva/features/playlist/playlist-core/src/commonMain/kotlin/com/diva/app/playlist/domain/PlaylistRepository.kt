package com.diva.app.playlist.domain

import com.diva.app.collection.playlist.models.ModerationStatus
import com.diva.app.collection.playlist.models.Playlist
import com.diva.app.collection.playlist.models.PlaylistSuggestions
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.models.User
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository : Repository {
    suspend fun get(collectionId: String): Result<Playlist>

    fun observe(collectionId: String): Flow<Result<Playlist>>

    suspend fun getContributors(collectionId: String): Result<List<User>>

    suspend fun addContributor(collectionId: String, contributorId: String): Result<Unit>

    suspend fun removeContributor(collectionId: String, contributorId: String): Result<Unit>

    suspend fun getSuggestions(collectionId: String): Result<List<PlaylistSuggestions>>

    suspend fun addSuggestion(collectionId: String, item: PlaylistSuggestions): Result<Unit>

    suspend fun updateSuggestionStatus(id: String, status: ModerationStatus): Result<Unit>

    suspend fun deleteSuggestion(id: String): Result<Unit>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(playlist: Playlist): Result<Unit>
}
