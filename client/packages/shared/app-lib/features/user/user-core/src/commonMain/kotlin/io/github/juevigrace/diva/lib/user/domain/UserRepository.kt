package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository : Repository {
    fun observe(): Flow<Result<List<User>>>

    fun observeCurrent(): Flow<Result<User>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(user: User): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
