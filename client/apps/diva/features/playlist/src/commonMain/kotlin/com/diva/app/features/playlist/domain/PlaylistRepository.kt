package com.diva.app.features.playlist.domain

import com.diva.app.models.collection.ModerationStatus
import com.diva.app.models.collection.playlist.Playlist
import com.diva.app.models.collection.playlist.PlaylistSuggestions
import io.github.juevigrace.diva.core.*
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface PlaylistRepository : Repository {
    suspend fun getPlaylist(collectionId: Uuid): Result<Option<Playlist>>

    fun getPlaylistFlow(collectionId: Uuid): Flow<Result<Option<Playlist>>>

    suspend fun getContributors(collectionId: Uuid): Result<List<User>>

    suspend fun addContributor(collectionId: Uuid, contributorId: Uuid): Result<Unit>

    suspend fun removeContributor(collectionId: Uuid, contributorId: Uuid): Result<Unit>

    suspend fun getSuggestions(collectionId: Uuid): Result<List<PlaylistSuggestions>>

    suspend fun addSuggestion(collectionId: Uuid, item: PlaylistSuggestions): Result<Unit>

    suspend fun updateSuggestionStatus(id: Uuid, status: ModerationStatus): Result<Unit>

    suspend fun deleteSuggestion(id: Uuid): Result<Unit>

    suspend fun sync(): Result<Unit>

    suspend fun save(playlist: Playlist): Result<Unit>
}