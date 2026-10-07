package com.diva.app.media.data

import com.diva.app.media.database.TagStorage
import com.diva.app.media.domain.TagRepository
import com.diva.app.media.tag.models.Tag
import io.github.juevigrace.diva.core.getOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TagRepositoryImpl(
    private val storage: TagStorage,
) : TagRepository {

    override fun observe(): Flow<Result<List<Tag>>> = storage.getAllFlow()

    override fun observe(id: String): Flow<Result<Tag>> = storage.getByIdFlow(id).map { result ->
        result.mapCatching { option ->
            option.getOrThrow { IllegalStateException("No tag '$id'") }
        }
    }

    override suspend fun getTagByName(name: String): Result<Tag> {
        return storage.getByName(name).mapCatching { option ->
            option.getOrThrow { IllegalStateException("No tag named '$name'") }
        }
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun upsert(tag: Tag): Result<Unit> = storage.upsert(tag)

    override suspend fun delete(id: String): Result<Unit> = storage.delete(id)
}
