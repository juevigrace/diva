package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.permissions.models.UserPermission

interface UserPermissionsRepository : Repository {
    suspend fun get(id: String): Result<List<UserPermission>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(permission: UserPermission): Result<Unit>

    suspend fun delete(permissionId: String): Result<Unit>
}
