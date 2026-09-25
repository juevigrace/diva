package com.diva.app.mix.data

import com.diva.app.mix.database.MixMetadataStorage
import com.diva.app.mix.domain.MixRepository
import com.diva.app.models.collection.mix.Mix
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow

class MixRepositoryImpl(
    private val storage: MixMetadataStorage,
) : MixRepository {

    override suspend fun getMix(collectionId: String): Result<Option<Mix>> {
        return storage.getByCollection(collectionId)
    }

    override fun getMixFlow(collectionId: String): Flow<Result<Option<Mix>>> {
        return storage.getByCollectionFlow(collectionId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(mix: Mix): Result<Unit> {
        return storage.upsert(mix.collection.id, mix)
    }

    override suspend fun deleteByCollection(collectionId: String): Result<Unit> {
        return storage.deleteByCollection(collectionId)
    }
}
