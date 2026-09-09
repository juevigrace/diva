package com.diva.app.features.playlist.database

import com.diva.app.database.DivaDB
import com.diva.app.database.collection.playlist.PlaylistSuggestionsStorage
import com.diva.app.models.collection.ModerationStatus
import com.diva.app.models.collection.playlist.PlaylistSuggestions
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import migrations.Diva_playlist_suggestions

@OptIn(ExperimentalUuidApi::class)
class PlaylistSuggestionsStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : PlaylistSuggestionsStorage {

    override suspend fun getByCollection(collectionId: Uuid): Result<List<PlaylistSuggestions>> {
        return db.getList { playlistSuggestionsQueries.findForCollection(collectionId.toString(), ::mapToSuggestions) }
    }

    override suspend fun add(collectionId: Uuid, item: PlaylistSuggestions): Result<Unit> {
        return db.use {
            transaction {
                playlistSuggestionsQueries.insert(
                    Diva_playlist_suggestions(
                        id = item.id.toString(),
                        collection_id = collectionId.toString(),
                        suggester_id = item.suggesterId.id.toString(),
                        media_id = item.mediaId.id.toString(),
                        status = item.status,
                        suggested_at = item.suggestedAt.epochSeconds,
                    )
                )
            }
        }
    }

    override suspend fun updateStatus(id: Uuid, status: ModerationStatus): Result<Unit> {
        return db.use {
            transaction {
                playlistSuggestionsQueries.updateStatus(status, id.toString())
            }
        }
    }

    override suspend fun delete(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                playlistSuggestionsQueries.delete(id.toString())
            }
        }
    }

    override suspend fun removeAllByCollection(collectionId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                playlistSuggestionsQueries.deleteByCollection(collectionId.toString())
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
        id = Uuid.parse(id),
        suggesterId = User(id = Uuid.parse(suggesterId)),
        mediaId = Media(id = Uuid.parse(mediaId), title = "", uri = ""),
        status = status,
        suggestedAt = Instant.fromEpochSeconds(suggestedAt),
    )
}