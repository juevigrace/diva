package com.diva.app.playlist.database

import com.diva.app.database.playlist.DivaDB
import com.diva.app.models.collection.ModerationStatus
import com.diva.app.models.collection.playlist.PlaylistSuggestions
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import migrations.Diva_playlist_suggestions

interface PlaylistSuggestionsStorage {
    suspend fun getByCollection(collectionId: String): Result<List<PlaylistSuggestions>>

    suspend fun add(collectionId: String, item: PlaylistSuggestions): Result<Unit>

    suspend fun updateStatus(id: String, status: ModerationStatus): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun removeAllByCollection(collectionId: String): Result<Unit>
}

class PlaylistSuggestionsStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : PlaylistSuggestionsStorage {

    override suspend fun getByCollection(collectionId: String): Result<List<PlaylistSuggestions>> {
        return db.getList { playlistSuggestionsQueries.findForCollection(collectionId, ::mapToSuggestions) }
    }

    override suspend fun add(collectionId: String, item: PlaylistSuggestions): Result<Unit> {
        return db.use {
            transaction {
                playlistSuggestionsQueries.insert(
                    Diva_playlist_suggestions(
                        id = item.id,
                        collection_id = collectionId,
                        suggester_id = item.suggesterId.id,
                        media_id = item.mediaId.id,
                        status = item.status,
                        suggested_at = item.suggestedAt,
                    )
                )
            }
        }
    }

    override suspend fun updateStatus(id: String, status: ModerationStatus): Result<Unit> {
        return db.use {
            transaction {
                playlistSuggestionsQueries.updateStatus(status, id)
            }
        }
    }

    override suspend fun delete(id: String): Result<Unit> {
        return db.use {
            transaction {
                playlistSuggestionsQueries.delete(id)
            }
        }
    }

    override suspend fun removeAllByCollection(collectionId: String): Result<Unit> {
        return db.use {
            transaction {
                playlistSuggestionsQueries.deleteByCollection(collectionId)
            }
        }
    }

    private fun mapToSuggestions(
        id: String,
        collectionId: String,
        suggesterId: String,
        mediaId: String,
        status: ModerationStatus,
        suggestedAt: Long,
    ): PlaylistSuggestions = PlaylistSuggestions(
        id = id,
        suggesterId = User(id = suggesterId),
        mediaId = Media(id = mediaId, title = "", uri = ""),
        status = status,
        suggestedAt = suggestedAt,
    )
}
