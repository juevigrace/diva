package com.diva.app.database.collection.mix

import com.diva.app.models.collection.mix.Mix
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface MixMetadataStorage {
    suspend fun getByCollection(collectionId: Uuid): Result<Option<Mix>>

    fun getByCollectionFlow(collectionId: Uuid): Flow<Result<Option<Mix>>>

    suspend fun upsert(collectionId: Uuid, item: Mix): Result<Unit>

    suspend fun deleteByCollection(collectionId: Uuid): Result<Unit>
}
