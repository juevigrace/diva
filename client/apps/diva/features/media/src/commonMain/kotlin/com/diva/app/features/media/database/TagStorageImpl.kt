package com.diva.app.features.media.database

import com.diva.app.database.DivaDB
import com.diva.app.database.media.TagStorage
import com.diva.app.models.media.tag.Tag
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import migrations.Diva_tag

@OptIn(ExperimentalUuidApi::class)
class TagStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : TagStorage {

    override suspend fun getAll(): Result<List<Tag>> {
        return db.getList { tagQueries.findAll(::mapToTag) }
    }

    override fun getAllFlow(): Flow<Result<List<Tag>>> {
        return db.getListAsFlow { tagQueries.findAll(::mapToTag) }
    }

    override suspend fun getById(id: Uuid): Result<Option<Tag>> {
        return db.getOne { tagQueries.findOneById(id.toString(), ::mapToTag) }
    }

    override fun getByIdFlow(id: Uuid): Flow<Result<Option<Tag>>> {
        return db.getOneAsFlow { tagQueries.findOneById(id.toString(), ::mapToTag) }
    }

    override suspend fun getByName(name: String): Result<Option<Tag>> {
        return db.getOne { tagQueries.findByName(name, ::mapToTag) }
    }

    override suspend fun upsert(item: Tag): Result<Unit> {
        return db.use {
            transaction {
                tagQueries.upsert(
                    Diva_tag(
                        id = item.id,
                        tag_name = item.name,
                        created_at = item.createdAt,
                        updated_at = item.updatedAt,
                        deleted_at = item.deletedAt.getOrNull(),
                    )
                )
            }
        }
    }

    override suspend fun delete(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                tagQueries.deleteById(id.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                tagQueries.findAll(::mapToTag)
                    .executeAsList()
                    .forEach { tag ->
                        tagQueries.deleteById(tag.id)
                    }
            }
        }
    }

    private fun mapToTag(
        id: String,
        name: String,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Tag = Tag(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = Option.of(deletedAt),
    )
}