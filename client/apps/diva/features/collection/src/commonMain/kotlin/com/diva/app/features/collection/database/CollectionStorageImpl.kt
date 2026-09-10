package com.diva.app.features.collection.database

import com.diva.app.database.DivaDB
import com.diva.app.database.collection.CollectionStorage
import com.diva.app.models.collection.Collection
import com.diva.app.models.collection.CollectionType
import com.diva.app.models.collection.VisibilityType
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import migrations.Diva_collection

@OptIn(ExperimentalUuidApi::class)
class CollectionStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : CollectionStorage {

    override suspend fun getAll(): Result<List<Collection>> {
        return db.getList { collectionQueries.findAll(::mapToCollection) }
    }

    override fun getAllFlow(): Flow<Result<List<Collection>>> {
        return db.getListAsFlow { collectionQueries.findAll(::mapToCollection) }
    }

    override suspend fun getById(id: Uuid): Result<Option<Collection>> {
        return db.getOne { collectionQueries.findOneById(id.toString(), ::mapToCollection) }
    }

    override fun getByIdFlow(id: Uuid): Flow<Result<Option<Collection>>> {
        return db.getOneAsFlow { collectionQueries.findOneById(id.toString(), ::mapToCollection) }
    }

    override suspend fun upsert(item: Collection): Result<Unit> {
        return db.use {
            transaction {
                collectionQueries.upsert(
                    Diva_collection(
                        id = item.id.toString(),
                        owner_id = item.owner.id.toString(),
                        name = item.name,
                        description = item.description,
                        collection_type = item.collectionType,
                        visibility = item.visibility,
                        cover_media_id = item.coverMedia.map { it.id.toString() }.getOrNull(),
                        created_at = item.createdAt.epochSeconds,
                        updated_at = item.updatedAt.epochSeconds,
                        deleted_at = item.deletedAt.map { it.epochSeconds }.getOrNull(),
                    )
                )
            }
        }
    }

    override suspend fun delete(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                collectionQueries.deleteById(id.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                collectionQueries.findAll(::mapToCollection)
                    .executeAsList()
                    .forEach { collection ->
                        collectionQueries.deleteById(collection.id.toString())
                    }
            }
        }
    }

    private fun mapToCollection(
        id: String,
        ownerId: String,
        name: String,
        description: String,
        collectionType: CollectionType,
        visibility: VisibilityType,
        coverMediaId: String?,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Collection = Collection(
        id = Uuid.parse(id),
        owner = User(id = Uuid.parse(ownerId)),
        name = name,
        description = description,
        collectionType = collectionType,
        visibility = visibility,
        coverMedia = Option.of(coverMediaId?.let { Media(id = Uuid.parse(it), title = "", uri = "") }),
        createdAt = Instant.fromEpochSeconds(createdAt),
        updatedAt = Instant.fromEpochSeconds(updatedAt),
        deletedAt = Option.of(deletedAt?.let { Instant.fromEpochSeconds(it) }),
    )
}