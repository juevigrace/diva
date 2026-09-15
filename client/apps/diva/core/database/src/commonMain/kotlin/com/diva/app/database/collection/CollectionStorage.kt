package com.diva.app.database.collection

import com.diva.app.models.collection.Collection
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface CollectionStorage {
    suspend fun getAll(): Result<List<Collection>>

    fun getAllFlow(): Flow<Result<List<Collection>>>

    suspend fun getById(id: Uuid): Result<Option<Collection>>

    fun getByIdFlow(id: Uuid): Flow<Result<Option<Collection>>>

    suspend fun upsert(item: Collection): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
