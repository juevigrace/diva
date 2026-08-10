package com.diva.collection.database.playlist

import com.diva.database.DivaDB
import com.diva.database.collection.playlist.PlaylistStorage
import com.diva.models.VisibilityType
import com.diva.models.collection.Collection
import com.diva.models.collection.CollectionType
import com.diva.models.collection.playlist.Playlist
import com.diva.models.media.Media
import com.diva.models.user.User
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.database.DatabaseAction
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.ErrorCause
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PlaylistStorageImpl(
    private val db: DivaDatabase<DivaDB>
) : PlaylistStorage {
    override suspend fun count(): DivaResult<Long, DivaError> {
        return db.use {
            val value: Long = playlistQueries.count().executeAsOne()
            DivaResult.success(value)
        }
    }

    override suspend fun getAll(
        limit: Int,
        offset: Int
    ): DivaResult<List<Playlist>, DivaError> {
        return db.getList { playlistQueries.findAll(limit.toLong(), offset.toLong(), mapper = ::mapToEntity) }
    }

    override fun getAllFlow(
        limit: Int,
        offset: Int
    ): Flow<DivaResult<List<Playlist>, DivaError>> {
        return db.getListAsFlow { playlistQueries.findAll(limit.toLong(), offset.toLong(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getById(id: Uuid): DivaResult<Option<Playlist>, DivaError> {
        return db.getOne { playlistQueries.findOneById(id.toString(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getByIdFlow(id: Uuid): Flow<DivaResult<Option<Playlist>, DivaError>> {
        return db.getOneAsFlow { playlistQueries.findOneById(id.toString(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun insert(item: Playlist): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                playlistQueries.insert(
                    collection_id = item.collection.id.toString(),
                    is_collaborative = item.isCollaborative,
                    allow_suggestions = item.allowSuggestions
                )
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.INSERT,
                            table = Option.Some("diva_playlist"),
                            details = Option.Some("Failed to insert")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun update(item: Playlist): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                playlistQueries.update(
                    is_collaborative = item.isCollaborative,
                    allow_suggestions = item.allowSuggestions,
                    collection_id = item.collection.id.toString(),
                )
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_playlist"),
                            details = Option.Some("Failed to update")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun delete(id: Uuid): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                playlistQueries.delete(id.toString())
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.DELETE,
                            table = Option.Some("diva_playlist"),
                            details = Option.Some("Failed to delete")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    private fun mapToEntity(
        owner: String,
        coverMediaId: String,
        name: String,
        description: String,
        collectionType: CollectionType,
        visibility: VisibilityType,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
        id: String?,
        isCollaborative: Boolean?,
        allowSuggestions: Boolean?,
    ): Playlist {
        require(id != null) { "Playlist id cannot be null" }
        require(isCollaborative != null) { "Playlist isCollaborative cannot be null" }
        require(allowSuggestions != null) { "Playlist allowSuggestions cannot be null" }

        return Playlist(
            collection = Collection(
                id = Uuid.parse(id),
                owner = User(id = Uuid.parse(owner)),
                coverMedia = Media(id = Uuid.parse(coverMediaId)),
                name = name,
                description = description,
                collectionType = collectionType,
                visibility = visibility,
                createdAt = Instant.fromEpochMilliseconds(createdAt),
                updatedAt = Instant.fromEpochMilliseconds(updatedAt),
                deletedAt = Option.of(deletedAt?.let { Instant.fromEpochMilliseconds(it) })
            ),
            isCollaborative = isCollaborative,
            allowSuggestions = allowSuggestions
        )
    }
}
