package io.github.juevigrace.diva.lib.database.user

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow

interface UserStorage {
    suspend fun findAll(): Result<List<User>>

    fun findAllFlow(): Flow<Result<List<User>>>

    suspend fun findOne(id: String): Result<Option<User>>

    fun findOneFlow(id: String): Flow<Result<Option<User>>>

    suspend fun upsert(item: User): Result<Unit>

    suspend fun deleteOne(id: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}
