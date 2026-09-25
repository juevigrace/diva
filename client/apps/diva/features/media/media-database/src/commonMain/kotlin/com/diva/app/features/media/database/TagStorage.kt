package com.diva.app.features.media.database

import com.diva.app.database.media.DivaDB
import com.diva.app.models.media.tag.Tag
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import migrations.Diva_tag

interface TagStorage {
    suspend fun getAll(): Result<List<Tag>>

    fun getAllFlow(): Flow<Result<List<Tag>>>

    suspend fun getById(id: String): Result<Option<Tag>>

    fun getByIdFlow(id: String): Flow<Result<Option<Tag>>>

    suspend fun getByName(name: String): Result<Option<Tag>>

    suspend fun upsert(item: Tag): Result<Unit>

    suspend fun delete(id: String): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}

class TagStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : TagStorage {

    override suspend fun getAll(): Result<List<Tag>> {
        return db.getList { tagQueries.findAll(::mapToTag) }
    }

    override fun getAllFlow(): Flow<Result<List<Tag>>> {
        return db.getListAsFlow { tagQueries.findAll(::mapToTag) }
    }

    override suspend fun getById(id: String): Result<Option<Tag>> {
        return db.getOne { tagQueries.findOneById(id, ::mapToTag) }
    }

    override fun getByIdFlow(id: String): Flow<Result<Option<Tag>>> {
        return db.getOneAsFlow { tagQueries.findOneById(id, ::mapToTag) }
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

    override suspend fun delete(id: String): Result<Unit> {
        return db.use {
            transaction {
                tagQueries.deleteById(id)
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
