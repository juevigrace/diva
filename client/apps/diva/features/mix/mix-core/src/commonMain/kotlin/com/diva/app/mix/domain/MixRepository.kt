package com.diva.app.mix.domain

import com.diva.app.collection.mix.models.Mix
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface MixRepository : Repository {
    suspend fun get(collectionId: String): Result<Mix>

    fun observe(collectionId: String): Flow<Result<Mix>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(mix: Mix): Result<Unit>

    suspend fun deleteByCollection(collectionId: String): Result<Unit>
}
