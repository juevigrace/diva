package com.diva.app.features.playlist.database

import com.diva.app.database.DivaDB
import com.diva.app.database.collection.playlist.PlaylistContributorStorage
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.uuid.Uuid
import migrations.Diva_playlist_contributor

class PlaylistContributorStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : PlaylistContributorStorage {

    override suspend fun getByCollection(collectionId: Uuid): Result<List<User>> {
        return db.getList { playlistContributorQueries.findForCollection(collectionId.toString()) { _, contributorId ->
            User(id = contributorId)
        } }
    }

    override suspend fun add(collectionId: Uuid, contributorId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                playlistContributorQueries.insert(
                    Diva_playlist_contributor(
                        collection_id = collectionId.toString(),
                        contributor_id = contributorId.toString(),
                    )
                )
            }
        }
    }

    override suspend fun remove(collectionId: Uuid, contributorId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                playlistContributorQueries.delete(collectionId.toString(), contributorId.toString())
            }
        }
    }

    override suspend fun removeAllByCollection(collectionId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                playlistContributorQueries.deleteByCollection(collectionId.toString())
            }
        }
    }
}