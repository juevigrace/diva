package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.permissions.UserPermission
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserPermissionsRepository : Repository {
    val client: DivaClient

    fun getPermissions(userId: String): Flow<Result<List<UserPermission>>>

    fun getPermission(permissionId: String, userId: String): Flow<Result<Option<UserPermission>>>

    suspend fun sync(userId: String): Result<Unit>

    suspend fun save(userId: String, permission: UserPermission): Result<Unit>

    suspend fun delete(permissionId: String, userId: String): Result<Unit>
}
