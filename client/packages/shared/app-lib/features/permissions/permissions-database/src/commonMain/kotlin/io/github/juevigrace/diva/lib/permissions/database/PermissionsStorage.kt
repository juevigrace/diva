package io.github.juevigrace.diva.lib.permissions.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.permissions.DivaSharedDB
import io.github.juevigrace.diva.lib.models.permission.Permission
import io.github.juevigrace.diva.lib.models.permission.PermissionAction
import io.github.juevigrace.diva.lib.models.Role
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

class PermissionsStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>,
) : PermissionsStorage {

    override suspend fun findAll(): Result<List<Permission>> {
        return db.getList {
            permissionsQueries.findAll(::mapToPermission)
        }
    }

    override fun findAllFlow(): Flow<Result<List<Permission>>> {
        return db.getListAsFlow {
            permissionsQueries.findAll(::mapToPermission)
        }
    }

    override suspend fun findOne(id: String): Result<Option<Permission>> {
        return db.getOne {
            permissionsQueries.findOne(id, ::mapToPermission)
        }
    }

    override fun findOneFlow(id: String): Flow<Result<Option<Permission>>> {
        return db.getOneAsFlow {
            permissionsQueries.findOne(id, ::mapToPermission)
        }
    }

    override suspend fun upsert(item: Permission): Result<Unit> {
        return db.use {
            transaction {
                permissionsQueries.upsert(
                    id = item.id,
                    name = item.name,
                    description = item.description,
                    action = item.action,
                    role_level = item.roleLevel,
                    created_at = item.createdAt,
                    updated_at = item.updatedAt
                )
            }
        }
    }

    override suspend fun deleteOne(id: String): Result<Unit> {
        return db.use {
            transaction {
                permissionsQueries.deleteOne(id)
            }
        }
    }

    override suspend fun delete(): Result<Unit> {
        return db.use {
            transaction {
                permissionsQueries.delete()
            }
        }
    }

    @Suppress("LongParameterList")
    private fun mapToPermission(
        id: String,
        name: String,
        description: String,
        action: PermissionAction,
        roleLevel: Role,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Permission {
        return Permission(
            id = id,
            name = name,
            description = description,
            action = action,
            roleLevel = roleLevel,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt.toOption()
        )
    }
}