package io.github.juevigrace.diva.lib.permissions.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.database.permissions.PermissionsStorage
import io.github.juevigrace.diva.lib.models.permission.Permission
import io.github.juevigrace.diva.lib.permissions.domain.PermissionsRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

class PermissionsRepositoryImpl(
    override val client: DivaClient,
    private val storage: PermissionsStorage,
) : PermissionsRepository {
    override fun getPermissions(): Flow<Result<List<Permission>>> = storage.findAllFlow()

    override fun getPermission(id: String): Flow<Result<Option<Permission>>> = storage.findOneFlow(id)

    override suspend fun sync(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun save(permission: Permission): Result<Unit> = storage.upsert(permission)

    override suspend fun delete(id: String): Result<Unit> = storage.deleteOne(id)
}
