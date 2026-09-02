package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.database.user.UserStorage
import io.github.juevigrace.diva.lib.models.user.User
import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserStorage,
) : UserRepository {
    override fun getUsers(): Flow<Result<List<User>>> = storage.getAllFlow()

    override fun getUser(id: Uuid): Flow<Result<Option<User>>> = storage.getByIdFlow(id)

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(user: User): Result<Unit> = storage.upsert(user)

    override suspend fun delete(id: Uuid): Result<Unit> = storage.delete(id)
}
