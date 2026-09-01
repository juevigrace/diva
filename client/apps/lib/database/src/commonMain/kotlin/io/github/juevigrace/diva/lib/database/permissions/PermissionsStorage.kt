package io.github.juevigrace.diva.lib.database.permissions

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.permission.Permission
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface PermissionsStorage {
    suspend fun getAll(): Result<List<Permission>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getById(id: Uuid): Result<Option<Permission>>

    suspend fun upsert(item: Permission): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(id: Uuid): Result<Unit>

    suspend fun deleteAll(): Result<Unit>
}
