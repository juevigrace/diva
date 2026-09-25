package com.diva.app.collection.database

import com.diva.app.database.collection.DivaDB
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
import migrations.Diva_collection

interface CollectionStorage {
    suspend fun getAll(): Result<List<Collection>>

    fun getAllFlow(): Flow<Result<List<Collection>>>

    suspend fun getById(id: String): Result<Option<Collection>>

    fun getByIdFlow(id: String): Flow<Result<Option<Collection>>>

    suspend fun upsert(item: Collection): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}

class CollectionStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : CollectionStorage {

    override suspend fun getAll(): Result<List<Collection>> {
        return db.getList { collectionQueries.findAll(::mapToCollection) }
    }

    override fun getAllFlow(): Flow<Result<List<Collection>>> {
        return db.getListAsFlow { collectionQueries.findAll(::mapToCollection) }
    }

    override suspend fun getById(id: String): Result<Option<Collection>> {
        return db.getOne { collectionQueries.findOneById(id, ::mapToCollection) }
    }

    override fun getByIdFlow(id: String): Flow<Result<Option<Collection>>> {
        return db.getOneAsFlow { collectionQueries.findOneById(id, ::mapToCollection) }
    }

    override suspend fun upsert(item: Collection): Result<Unit> {
        return db.use {
            transaction {
                collectionQueries.upsert(
                    Diva_collection(
                        id = item.id,
                        owner_id = item.owner.id,
                        name = item.name,
                        description = item.description,
                        collection_type = item.collectionType,
                        visibility = item.visibility,
                        cover_media_id = item.coverMedia.map { it.id }.getOrNull(),
                        created_at = item.createdAt,
                        updated_at = item.updatedAt,
                        deleted_at = item.deletedAt.getOrNull(),
                    )
                )
            }
        }
    }

    override suspend fun delete(id: String): Result<Unit> {
        return db.use {
            transaction {
                collectionQueries.deleteById(id)
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                collectionQueries.findAll(::mapToCollection)
                    .executeAsList()
                    .forEach { collection ->
                        collectionQueries.deleteById(collection.id)
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
        id = id,
        owner = User(id = ownerId),
        name = name,
        description = description,
        collectionType = collectionType,
        visibility = visibility,
        coverMedia = Option.of(coverMediaId?.let { Media(id = it, title = "", uri = "") }),
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = Option.of(deletedAt),
    )
}
