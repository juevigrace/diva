package io.github.juevigrace.diva.lib.permissions.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.permission.Permission
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface PermissionsRepository : Repository {
    val client: DivaClient

    fun getPermissions(): Flow<Result<List<Permission>>>

    fun getPermission(id: Uuid): Flow<Result<Option<Permission>>>

    suspend fun sync(): Result<Unit>

    suspend fun save(permission: Permission): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>
}
