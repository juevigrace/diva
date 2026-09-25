package com.diva.app.playlist.domain

import com.diva.app.models.collection.ModerationStatus
import com.diva.app.models.collection.playlist.Playlist
import com.diva.app.models.collection.playlist.PlaylistSuggestions
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository : Repository {
    suspend fun getPlaylist(collectionId: String): Result<Option<Playlist>>

    fun getPlaylistFlow(collectionId: String): Flow<Result<Option<Playlist>>>

    suspend fun getContributors(collectionId: String): Result<List<User>>

    suspend fun addContributor(collectionId: String, contributorId: String): Result<Unit>

    suspend fun removeContributor(collectionId: String, contributorId: String): Result<Unit>

    suspend fun getSuggestions(collectionId: String): Result<List<PlaylistSuggestions>>

    suspend fun addSuggestion(collectionId: String, item: PlaylistSuggestions): Result<Unit>

    suspend fun updateSuggestionStatus(id: String, status: ModerationStatus): Result<Unit>

    suspend fun deleteSuggestion(id: String): Result<Unit>

    suspend fun sync(): Result<Unit>

    suspend fun save(playlist: Playlist): Result<Unit>
}
