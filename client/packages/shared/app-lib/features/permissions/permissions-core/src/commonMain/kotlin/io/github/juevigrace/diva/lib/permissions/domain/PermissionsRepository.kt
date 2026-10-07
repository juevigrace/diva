package io.github.juevigrace.diva.lib.permissions.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.permission.models.Permission
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface PermissionsRepository : Repository {
    val client: DivaClient

    fun observe(): Flow<Result<List<Permission>>>

    fun observe(id: String): Flow<Result<Permission>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(permission: Permission): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
