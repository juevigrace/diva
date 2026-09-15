package io.github.juevigrace.diva.lib.database.permissions

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.permission.Permission
import kotlinx.coroutines.flow.Flow

interface PermissionsStorage {
    suspend fun findAll(): Result<List<Permission>>

    fun findAllFlow(): Flow<Result<List<Permission>>>

    suspend fun findOne(id: String): Result<Option<Permission>>

    fun findOneFlow(id: String): Flow<Result<Option<Permission>>>

    suspend fun upsert(item: Permission): Result<Unit>

    suspend fun deleteOne(id: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}
