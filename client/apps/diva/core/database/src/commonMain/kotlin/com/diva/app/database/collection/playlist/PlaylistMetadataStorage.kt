package com.diva.app.database.collection.playlist

import com.diva.app.models.collection.playlist.Playlist
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface PlaylistMetadataStorage {
    suspend fun getByCollection(collectionId: Uuid): Result<Option<Playlist>>

    fun getByCollectionFlow(collectionId: Uuid): Flow<Result<Option<Playlist>>>

    suspend fun upsert(collectionId: Uuid, item: Playlist): Result<Unit>

    suspend fun deleteByCollection(collectionId: Uuid): Result<Unit>
}
