package com.diva.app.features.media.data

import com.diva.app.features.media.database.TagStorage
import com.diva.app.features.media.domain.TagRepository
import com.diva.app.models.media.tag.Tag
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow

class TagRepositoryImpl(
    private val storage: TagStorage,
) : TagRepository {

    override fun getTags(): Flow<Result<List<Tag>>> = storage.getAllFlow()

    override fun getTag(id: String): Flow<Result<Option<Tag>>> = storage.getByIdFlow(id)

    override suspend fun getTagByName(name: String): Result<Option<Tag>> {
        return storage.getByName(name)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(tag: Tag): Result<Unit> = storage.upsert(tag)

    override suspend fun delete(id: String): Result<Unit> = storage.delete(id)
}
