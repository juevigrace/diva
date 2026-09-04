package io.github.juevigrace.diva.lib.permissions.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.DivaSharedDB
import io.github.juevigrace.diva.lib.database.permissions.PermissionsStorage
import io.github.juevigrace.diva.lib.models.permission.Permission
import io.github.juevigrace.diva.lib.models.permission.safePermissionAction
import io.github.juevigrace.diva.lib.models.user.Role
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class PermissionsStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>,
) : PermissionsStorage {

    override suspend fun getAll(): Result<List<Permission>> {
        return db.getList {
            permissionsQueries.findAll(::mapToPermission)
        }
    }

    override fun getAllFlow(): Flow<Result<List<Permission>>> {
        return db.getListAsFlow {
            permissionsQueries.findAll(::mapToPermission)
        }
    }

    override suspend fun getById(id: Uuid): Result<Option<Permission>> {
        return db.getOne {
            permissionsQueries.findOneById(id.toString(), ::mapToPermission)
        }
    }

    override fun getByIdFlow(id: Uuid): Flow<Result<Option<Permission>>> {
        return db.getOneAsFlow {
            permissionsQueries.findOneById(id.toString(), ::mapToPermission)
        }
    }

    override suspend fun upsert(item: Permission): Result<Unit> {
        return db.use {
            transaction {
                permissionsQueries.upsert(
                    id = item.id.toString(),
                    name = item.name,
                    description = item.description,
                    action = item.action.toString(),
                    role_level = item.roleLevel,
                    created_at = item.createdAt.epochSeconds,
                    updated_at = item.updatedAt.epochSeconds
                )
            }
        }
    }

    override suspend fun delete(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                permissionsQueries.deleteById(id.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                permissionsQueries.deleteAll()
            }
        }
    }

    @Suppress("LongParameterList")
    private fun mapToPermission(
        id: String,
        name: String,
        description: String,
        action: String,
        roleLevel: Role,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): Permission {
        return Permission(
            id = Uuid.parse(id),
            name = name,
            description = description,
            action = safePermissionAction(action),
            roleLevel = roleLevel,
            createdAt = Instant.fromEpochSeconds(createdAt),
            updatedAt = Instant.fromEpochSeconds(updatedAt),
            deletedAt = deletedAt?.let { Instant.fromEpochSeconds(it) }.toOption()
        )
    }
}
