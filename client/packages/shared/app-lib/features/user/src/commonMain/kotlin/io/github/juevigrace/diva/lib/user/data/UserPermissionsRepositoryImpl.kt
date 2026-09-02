package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.database.user.permissions.UserPermissionsStorage
import io.github.juevigrace.diva.lib.models.user.permissions.UserPermission
import io.github.juevigrace.diva.lib.user.domain.UserPermissionsRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserPermissionsRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserPermissionsStorage,
) : UserPermissionsRepository {
    override fun getPermissions(userId: Uuid): Flow<Result<List<UserPermission>>> = storage.getAllByUserFlow(userId)

    override fun getPermission(permissionId: Uuid, userId: Uuid): Flow<Result<Option<UserPermission>>> =
        storage.getByIdFlow(permissionId, userId)

    override suspend fun sync(userId: Uuid): Result<Unit> = Result.success(Unit)

    override suspend fun save(permission: UserPermission): Result<Unit> = storage.upsert(permission)

    override suspend fun delete(permissionId: Uuid, userId: Uuid): Result<Unit> = storage.delete(permissionId, userId)
}
