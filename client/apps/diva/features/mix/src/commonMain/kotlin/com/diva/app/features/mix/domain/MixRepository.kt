package com.diva.app.features.mix.domain

import com.diva.app.models.collection.mix.Mix
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface MixRepository : Repository {
    suspend fun getMix(collectionId: Uuid): Result<Option<Mix>>

    fun getMixFlow(collectionId: Uuid): Flow<Result<Option<Mix>>>

    suspend fun sync(): Result<Unit>

    suspend fun save(mix: Mix): Result<Unit>

    suspend fun deleteByCollection(collectionId: Uuid): Result<Unit>
}