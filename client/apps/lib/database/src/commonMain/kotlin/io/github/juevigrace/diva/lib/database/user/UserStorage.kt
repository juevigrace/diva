package io.github.juevigrace.diva.lib.database.user

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserStorage {
    suspend fun getAll(): Result<List<User>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(id: Uuid): Result<Option<User>>

    suspend fun upsert(item: User): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
