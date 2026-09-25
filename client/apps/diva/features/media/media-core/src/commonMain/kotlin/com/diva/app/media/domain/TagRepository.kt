package com.diva.app.media.domain

import com.diva.app.models.media.tag.Tag
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface TagRepository : Repository {
    fun getTags(): Flow<Result<List<Tag>>>

    fun getTag(id: String): Flow<Result<Option<Tag>>>

    suspend fun getTagByName(name: String): Result<Option<Tag>>

    suspend fun sync(): Result<Unit>

    suspend fun save(tag: Tag): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
