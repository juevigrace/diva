package com.diva.app.features.playlist.data

import com.diva.app.database.collection.playlist.PlaylistContributorStorage
import com.diva.app.database.collection.playlist.PlaylistMetadataStorage
import com.diva.app.database.collection.playlist.PlaylistSuggestionsStorage
import com.diva.app.features.playlist.domain.PlaylistRepository
import com.diva.app.models.collection.ModerationStatus
import com.diva.app.models.collection.playlist.Playlist
import com.diva.app.models.collection.playlist.PlaylistSuggestions
import io.github.juevigrace.diva.core.*
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class PlaylistRepositoryImpl(
    private val metadataStorage: PlaylistMetadataStorage,
    private val contributorStorage: PlaylistContributorStorage,
    private val suggestionsStorage: PlaylistSuggestionsStorage,
) : PlaylistRepository {

    override suspend fun getPlaylist(collectionId: Uuid): Result<Option<Playlist>> {
        val metadata = metadataStorage.getByCollection(collectionId)
        val contributors = contributorStorage.getByCollection(collectionId)
        val suggestions = suggestionsStorage.getByCollection(collectionId)

        return metadata.map { option ->
            option.map { playlist ->
                playlist.copy(
                    contributors = contributors.getOrElse { emptyList() },
                    suggestions = suggestions.getOrElse { emptyList() },
                )
            }
        }
    }

    override fun getPlaylistFlow(collectionId: Uuid): Flow<Result<Option<Playlist>>> {
        return flow { emit(getPlaylist(collectionId)) }
    }

    override suspend fun getContributors(collectionId: Uuid): Result<List<User>> {
        return contributorStorage.getByCollection(collectionId)
    }

    override suspend fun addContributor(collectionId: Uuid, contributorId: Uuid): Result<Unit> {
        return contributorStorage.add(collectionId, contributorId)
    }

    override suspend fun removeContributor(collectionId: Uuid, contributorId: Uuid): Result<Unit> {
        return contributorStorage.remove(collectionId, contributorId)
    }

    override suspend fun getSuggestions(collectionId: Uuid): Result<List<PlaylistSuggestions>> {
        return suggestionsStorage.getByCollection(collectionId)
    }

    override suspend fun addSuggestion(collectionId: Uuid, item: PlaylistSuggestions): Result<Unit> {
        return suggestionsStorage.add(collectionId, item)
    }

    override suspend fun updateSuggestionStatus(id: Uuid, status: ModerationStatus): Result<Unit> {
        return suggestionsStorage.updateStatus(id, status)
    }

    override suspend fun deleteSuggestion(id: Uuid): Result<Unit> {
        return suggestionsStorage.delete(id)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(playlist: Playlist): Result<Unit> {
        val collectionId = playlist.collection.id
        return metadataStorage.upsert(collectionId, playlist)
            .onSuccess {
                playlist.contributors.forEach { contributor ->
                    contributorStorage.add(collectionId, contributor.id)
                }
                playlist.suggestions.forEach { suggestion ->
                    suggestionsStorage.add(collectionId, suggestion)
                }
            }
    }
}