package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.permissions.models.UserPermission
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserPermissionsRepository : Repository {
    val client: DivaClient

    fun observe(): Flow<Result<List<UserPermission>>>

    fun observe(permissionId: String): Flow<Result<UserPermission>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(permission: UserPermission): Result<Unit>

    suspend fun delete(permissionId: String): Result<Unit>
}
