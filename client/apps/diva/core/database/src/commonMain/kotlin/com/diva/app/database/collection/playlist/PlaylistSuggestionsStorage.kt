package com.diva.app.database.collection.playlist

import com.diva.app.models.collection.ModerationStatus
import com.diva.app.models.collection.playlist.PlaylistSuggestions
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface PlaylistSuggestionsStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getByCollection(collectionId: Uuid): Result<List<PlaylistSuggestions>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun add(collectionId: Uuid, item: PlaylistSuggestions): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun updateStatus(id: Uuid, status: ModerationStatus): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun removeAllByCollection(collectionId: Uuid): Result<Unit>
}
