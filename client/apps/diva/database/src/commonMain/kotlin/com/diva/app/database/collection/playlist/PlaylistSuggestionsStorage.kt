package com.diva.app.database.collection.playlist

import com.diva.app.models.collection.ModerationStatus
import com.diva.app.models.collection.playlist.PlaylistSuggestions
import kotlin.uuid.Uuid

interface PlaylistSuggestionsStorage {
    suspend fun getByCollection(collectionId: Uuid): Result<List<PlaylistSuggestions>>

    suspend fun add(collectionId: Uuid, item: PlaylistSuggestions): Result<Unit>

    suspend fun updateStatus(id: Uuid, status: ModerationStatus): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun removeAllByCollection(collectionId: Uuid): Result<Unit>
}
