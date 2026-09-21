package com.diva.app.features.playlist.database

import com.diva.app.database.playlist.DivaDB
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import migrations.Diva_playlist_contributor

interface PlaylistContributorStorage {
    suspend fun getByCollection(collectionId: String): Result<List<User>>

    suspend fun add(collectionId: String, contributorId: String): Result<Unit>

    suspend fun remove(collectionId: String, contributorId: String): Result<Unit>

    suspend fun removeAllByCollection(collectionId: String): Result<Unit>
}

class PlaylistContributorStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : PlaylistContributorStorage {

    override suspend fun getByCollection(collectionId: String): Result<List<User>> {
        return db.getList {
            playlistContributorQueries.findForCollection(collectionId) { _, contributorId ->
                User(id = contributorId)
            }
        }
    }

    override suspend fun add(collectionId: String, contributorId: String): Result<Unit> {
        return db.use {
            transaction {
                playlistContributorQueries.insert(
                    Diva_playlist_contributor(
                        collection_id = collectionId,
                        contributor_id = contributorId,
                    )
                )
            }
        }
    }

    override suspend fun remove(collectionId: String, contributorId: String): Result<Unit> {
        return db.use {
            transaction {
                playlistContributorQueries.delete(collectionId, contributorId)
            }
        }
    }

    override suspend fun removeAllByCollection(collectionId: String): Result<Unit> {
        return db.use {
            transaction {
                playlistContributorQueries.deleteByCollection(collectionId)
            }
        }
    }
}
