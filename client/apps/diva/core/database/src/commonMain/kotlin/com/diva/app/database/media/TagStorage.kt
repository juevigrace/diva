package com.diva.app.database.media

import com.diva.app.models.media.tag.Tag
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface TagStorage {
    suspend fun getAll(): Result<List<Tag>>

    fun getAllFlow(): Flow<Result<List<Tag>>>

    suspend fun getById(id: Uuid): Result<Option<Tag>>

    fun getByIdFlow(id: Uuid): Flow<Result<Option<Tag>>>

    suspend fun getByName(name: String): Result<Option<Tag>>

    suspend fun upsert(item: Tag): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
