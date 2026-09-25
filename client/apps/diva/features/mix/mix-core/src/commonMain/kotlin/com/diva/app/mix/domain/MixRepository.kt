package com.diva.app.mix.domain

import com.diva.app.models.collection.mix.Mix
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface MixRepository : Repository {
    suspend fun getMix(collectionId: String): Result<Option<Mix>>

    fun getMixFlow(collectionId: String): Flow<Result<Option<Mix>>>

    suspend fun sync(): Result<Unit>

    suspend fun save(mix: Mix): Result<Unit>

    suspend fun deleteByCollection(collectionId: String): Result<Unit>
}
