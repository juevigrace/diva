package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.permissions.UserPermission
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface UserPermissionsRepository : Repository {
    val client: DivaClient

    fun getPermissions(userId: Uuid): Flow<Result<List<UserPermission>>>

    fun getPermission(permissionId: Uuid, userId: Uuid): Flow<Result<Option<UserPermission>>>

    suspend fun sync(userId: Uuid): Result<Unit>

    suspend fun save(permission: UserPermission): Result<Unit>

    suspend fun delete(permissionId: Uuid, userId: Uuid): Result<Unit>
}
