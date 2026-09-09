package com.diva.app.features.media.domain

import com.diva.app.models.media.tag.Tag
import io.github.juevigrace.diva.core.*
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface TagRepository : Repository {
    fun getTags(): Flow<Result<List<Tag>>>

    fun getTag(id: Uuid): Flow<Result<Option<Tag>>>

    suspend fun getTagByName(name: String): Result<Option<Tag>>

    suspend fun sync(): Result<Unit>

    suspend fun save(tag: Tag): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>
}