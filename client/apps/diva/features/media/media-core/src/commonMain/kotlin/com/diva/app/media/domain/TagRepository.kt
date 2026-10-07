package com.diva.app.media.domain

import com.diva.app.media.tag.models.Tag
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface TagRepository : Repository {
    fun observe(): Flow<Result<List<Tag>>>

    fun observe(id: String): Flow<Result<Tag>>

    suspend fun getTagByName(name: String): Result<Tag>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(tag: Tag): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
