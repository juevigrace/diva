package com.diva.app.database.collection.playlist

import com.diva.app.models.collection.playlist.Playlist
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface PlaylistMetadataStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getByCollection(collectionId: Uuid): Result<Option<Playlist>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByCollectionFlow(collectionId: Uuid): Flow<Result<Option<Playlist>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun upsert(collectionId: Uuid, item: Playlist): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun deleteByCollection(collectionId: Uuid): Result<Unit>
}
