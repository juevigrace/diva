package com.diva.app.features.playlist.database

import com.diva.app.database.playlist.DivaDB
import com.diva.app.models.collection.Collection
import com.diva.app.models.collection.playlist.Playlist
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import migrations.Diva_playlist_metadata

interface PlaylistMetadataStorage {
    suspend fun getByCollection(collectionId: String): Result<Option<Playlist>>

    fun getByCollectionFlow(collectionId: String): Flow<Result<Option<Playlist>>>

    suspend fun upsert(collectionId: String, item: Playlist): Result<Unit>

    suspend fun deleteByCollection(collectionId: String): Result<Unit>
}

class PlaylistMetadataStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : PlaylistMetadataStorage {

    override suspend fun getByCollection(collectionId: String): Result<Option<Playlist>> {
        return db.getOne { playlistMetadataQueries.findByCollection(collectionId, ::mapToPlaylist) }
    }

    override fun getByCollectionFlow(collectionId: String): Flow<Result<Option<Playlist>>> {
        return db.getOneAsFlow { playlistMetadataQueries.findByCollection(collectionId, ::mapToPlaylist) }
    }

    override suspend fun upsert(collectionId: String, item: Playlist): Result<Unit> {
        return db.use {
            transaction {
                playlistMetadataQueries.upsert(
                    Diva_playlist_metadata(
                        collection_id = collectionId,
                        is_collaborative = item.isCollaborative,
                        allow_suggestions = item.allowSuggestions,
                    )
                )
            }
        }
    }

    override suspend fun deleteByCollection(collectionId: String): Result<Unit> {
        return db.use {
            transaction {
                playlistMetadataQueries.deleteByCollection(collectionId)
            }
        }
    }

    private fun mapToPlaylist(
        collectionId: String,
        isCollaborative: Boolean,
        allowSuggestions: Boolean,
    ): Playlist = Playlist(
        collection = Collection(id = collectionId),
        isCollaborative = isCollaborative,
        allowSuggestions = allowSuggestions,
    )
}
