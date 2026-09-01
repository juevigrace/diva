package io.github.juevigrace.diva.lib.database.user.permissions

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.permissions.UserPermission
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserPermissionsStorage {
    suspend fun getAllByUser(userId: Uuid): Result<List<UserPermission>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(permissionId: Uuid, userId: Uuid): Result<Option<UserPermission>>

    suspend fun upsert(item: UserPermission): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(permissionId: Uuid, userId: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
