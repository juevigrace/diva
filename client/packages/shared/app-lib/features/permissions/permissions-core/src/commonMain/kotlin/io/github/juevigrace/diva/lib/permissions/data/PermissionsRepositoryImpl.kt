package io.github.juevigrace.diva.lib.permissions.data

import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.lib.permission.models.Permission
import io.github.juevigrace.diva.lib.permissions.database.PermissionsStorage
import io.github.juevigrace.diva.lib.permissions.domain.PermissionsRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PermissionsRepositoryImpl(
    override val client: DivaClient,
    private val storage: PermissionsStorage,
) : PermissionsRepository {
    override fun observe(): Flow<Result<List<Permission>>> = storage.findAllFlow()

    override fun observe(id: String): Flow<Result<Permission>> = storage.findOneFlow(id).map { result ->
        result.mapCatching { option ->
            option.getOrThrow { IllegalStateException("No permission '$id'") }
        }
    }

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun upsert(permission: Permission): Result<Unit> = storage.upsert(permission)

    override suspend fun delete(id: String): Result<Unit> = storage.deleteOne(id)
}
