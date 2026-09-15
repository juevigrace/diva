package com.diva.app.features.mix.data

import com.diva.app.database.collection.mix.MixMetadataStorage
import com.diva.app.features.mix.domain.MixRepository
import com.diva.app.models.collection.mix.Mix
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

class MixRepositoryImpl(
    private val storage: MixMetadataStorage,
) : MixRepository {

    override suspend fun getMix(collectionId: Uuid): Result<Option<Mix>> {
        return storage.getByCollection(collectionId)
    }

    override fun getMixFlow(collectionId: Uuid): Flow<Result<Option<Mix>>> {
        return storage.getByCollectionFlow(collectionId)
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(mix: Mix): Result<Unit> {
        return storage.upsert(Uuid.parse(mix.collection.id), mix)
    }

    override suspend fun deleteByCollection(collectionId: Uuid): Result<Unit> {
        return storage.deleteByCollection(collectionId)
    }
}