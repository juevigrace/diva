package io.github.juevigrace.diva.lib.permissions.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.permission.Permission
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface PermissionsRepository : Repository {
    val client: DivaClient

    fun getPermissions(): Flow<Result<List<Permission>>>

    fun getPermission(id: String): Flow<Result<Option<Permission>>>

    suspend fun sync(): Result<Unit>

    suspend fun save(permission: Permission): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
