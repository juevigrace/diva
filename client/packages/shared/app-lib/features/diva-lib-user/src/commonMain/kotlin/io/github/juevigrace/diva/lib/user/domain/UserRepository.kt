package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.User
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface UserRepository : Repository {
    val client: DivaClient

    fun getUsers(): Flow<Result<List<User>>>

    fun getUser(id: Uuid): Flow<Result<Option<User>>>

    suspend fun sync(): Result<Unit>

    suspend fun save(user: User): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>
}
