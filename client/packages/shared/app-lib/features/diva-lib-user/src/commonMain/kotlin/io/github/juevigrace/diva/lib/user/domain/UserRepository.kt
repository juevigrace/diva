package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.User
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserRepository : Repository {
    val client: DivaClient

    fun getUsers(): Flow<Result<List<User>>>

    fun getUser(id: String): Flow<Result<Option<User>>>

    suspend fun sync(): Result<Unit>

    suspend fun save(user: User): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
