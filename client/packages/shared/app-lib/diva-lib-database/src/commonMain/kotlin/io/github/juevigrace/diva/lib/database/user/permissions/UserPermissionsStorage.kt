package io.github.juevigrace.diva.lib.database.user.permissions

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.permissions.UserPermission
import kotlinx.coroutines.flow.Flow

interface UserPermissionsStorage {
    suspend fun findAll(userId: String): Result<List<UserPermission>>

    fun findAllFlow(userId: String): Flow<Result<List<UserPermission>>>

    suspend fun findOne(permissionId: String, userId: String): Result<Option<UserPermission>>

    fun findOneFlow(permissionId: String, userId: String): Flow<Result<Option<UserPermission>>>

    suspend fun upsert(userId: String, item: UserPermission): Result<Unit>

    suspend fun deleteOne(permissionId: String, userId: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}
