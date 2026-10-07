package com.diva.app.mix.data

import com.diva.app.collection.mix.models.Mix
import com.diva.app.mix.database.MixMetadataStorage
import com.diva.app.mix.domain.MixRepository
import io.github.juevigrace.diva.core.getOrThrow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MixRepositoryImpl(
    private val storage: MixMetadataStorage,
) : MixRepository {

    override suspend fun get(collectionId: String): Result<Mix> {
        return storage.getByCollection(collectionId).mapCatching { option ->
            option.getOrThrow { IllegalStateException("No mix for collection '$collectionId'") }
        }
    }

    override fun observe(collectionId: String): Flow<Result<Mix>> = flow { emit(get(collectionId)) }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun upsert(mix: Mix): Result<Unit> {
        return storage.upsert(mix.collection.id, mix)
    }

    override suspend fun deleteByCollection(collectionId: String): Result<Unit> {
        return storage.deleteByCollection(collectionId)
    }
}
