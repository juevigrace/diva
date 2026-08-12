package com.diva.collection.database

import com.diva.database.DivaDB
import com.diva.database.collection.CollectionStorage
import com.diva.models.VisibilityType
import com.diva.models.collection.Collection
import com.diva.models.collection.CollectionType
import com.diva.models.media.Media
import com.diva.models.user.User
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.database.DatabaseAction
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.ErrorCause
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinInstant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

class CollectionStorageImpl(
    private val db: DivaDatabase<DivaDB>
) : CollectionStorage {
    override suspend fun count(): DivaResult<Long, DivaError> {
        return db.use {
            val value: Long = collectionQueries.count().executeAsOne()
            DivaResult.success(value)
        }
    }

    override suspend fun getAll(
        limit: Int,
        offset: Int
    ): DivaResult<List<Collection>, DivaError> {
        return db.getList { collectionQueries.findAll(limit.toLong(), offset.toLong(), mapper = ::mapToEntity) }
    }

    override fun getAllFlow(
        limit: Int,
        offset: Int
    ): Flow<DivaResult<List<Collection>, DivaError>> {
        return db.getListAsFlow { collectionQueries.findAll(limit.toLong(), offset.toLong(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getById(id: Uuid): DivaResult<Option<Collection>, DivaError> {
        return db.getOne { collectionQueries.findOneById(id.toJavaUuid(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getByIdFlow(id: Uuid): Flow<DivaResult<Option<Collection>, DivaError>> {
        return db.getOneAsFlow { collectionQueries.findOneById(id.toJavaUuid(), mapper = ::mapToEntity) }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun insert(item: Collection): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                collectionQueries.insert(
                    id = item.id.toJavaUuid(),
                    owner = item.owner.id.toJavaUuid(),
                    cover_media_id = item.coverMedia.id.toJavaUuid(),
                    name = item.name,
                    description = item.description,
                    collection_type = item.collectionType,
                    visibility = item.visibility,
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.INSERT,
                            table = Option.Some("diva_collection"),
                            details = Option.Some("Failed to insert")
                        )
                    )
                )
            }
            DivaResult.success(Unit)
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override suspend fun update(item: Collection): DivaResult<Unit, DivaError> {
        return db.use {
            val rows: Long = transactionWithResult {
                collectionQueries.update(
                    cover_media_id = item.coverMedia.id.toJavaUuid(),
                    name = item.name,
                    description = item.description,
                    visibility = item.visibility,
                    id = item.id.toJavaUuid()
                ).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.UPDATE,
                            table = Option.Some("diva_collection"),
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
                collectionQueries.delete(id.toJavaUuid()).value
            }
            if (rows.toInt() == 0) {
                return@use DivaResult.failure(
                    DivaError(
                        ErrorCause.Database.NoRowsAffected(
                            action = DatabaseAction.DELETE,
                            table = Option.Some("diva_collection"),
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
        id: UUID,
        owner: UUID,
        coverMediaId: UUID,
        name: String,
        description: String,
        collectionType: CollectionType,
        visibility: VisibilityType,
        createdAt: OffsetDateTime,
        updatedAt: OffsetDateTime,
        deletedAt: OffsetDateTime?,
    ): Collection {
        return Collection(
            id = id.toKotlinUuid(),
            owner = User(id = owner.toKotlinUuid()),
            coverMedia = Media(id = coverMediaId.toKotlinUuid()),
            name = name,
            description = description,
            collectionType = collectionType,
            visibility = visibility,
            createdAt = createdAt.toInstant().toKotlinInstant(),
            updatedAt = updatedAt.toInstant().toKotlinInstant(),
            deletedAt = Option.of(deletedAt?.toInstant()?.toKotlinInstant()),
        )
    }
}
