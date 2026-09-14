package com.diva.app.features.playlist.database

import com.diva.app.database.DivaDB
import com.diva.app.database.collection.playlist.PlaylistMetadataStorage
import com.diva.app.models.collection.Collection
import com.diva.app.models.collection.playlist.Playlist
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import migrations.Diva_playlist_metadata

@OptIn(ExperimentalUuidApi::class)
class PlaylistMetadataStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : PlaylistMetadataStorage {

    override suspend fun getByCollection(collectionId: Uuid): Result<Option<Playlist>> {
        return db.getOne { playlistMetadataQueries.findByCollection(collectionId.toString(), ::mapToPlaylist) }
    }

    override fun getByCollectionFlow(collectionId: Uuid): Flow<Result<Option<Playlist>>> {
        return db.getOneAsFlow { playlistMetadataQueries.findByCollection(collectionId.toString(), ::mapToPlaylist) }
    }

    override suspend fun upsert(collectionId: Uuid, item: Playlist): Result<Unit> {
        return db.use {
            transaction {
                playlistMetadataQueries.upsert(
                    Diva_playlist_metadata(
                        collection_id = collectionId.toString(),
                        is_collaborative = item.isCollaborative,
                        allow_suggestions = item.allowSuggestions,
                    )
                )
            }
        }
    }

    override suspend fun deleteByCollection(collectionId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                playlistMetadataQueries.deleteByCollection(collectionId.toString())
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