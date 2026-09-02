package io.github.juevigrace.diva.lib.database.user

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserStorage {
    suspend fun getAll(): Result<List<User>>

    fun getAllFlow(): Flow<Result<List<User>>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(id: Uuid): Result<Option<User>>

    @OptIn(ExperimentalUuidApi::class)
    fun getByIdFlow(id: Uuid): Flow<Result<Option<User>>>

    suspend fun upsert(item: User): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
