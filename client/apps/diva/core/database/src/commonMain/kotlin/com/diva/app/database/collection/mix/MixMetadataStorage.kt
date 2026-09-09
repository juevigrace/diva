package com.diva.app.database.collection.mix

import com.diva.app.models.collection.mix.Mix
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface MixMetadataStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getByCollection(collectionId: Uuid): Result<Option<Mix>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByCollectionFlow(collectionId: Uuid): Flow<Result<Option<Mix>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun upsert(collectionId: Uuid, item: Mix): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun deleteByCollection(collectionId: Uuid): Result<Unit>
}
