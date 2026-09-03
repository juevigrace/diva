package io.github.juevigrace.diva.lib.user.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.DivaDB
import io.github.juevigrace.diva.lib.database.user.permissions.UserPermissionsStorage
import io.github.juevigrace.diva.lib.models.permission.Permission
import io.github.juevigrace.diva.lib.models.permission.PermissionAction
import io.github.juevigrace.diva.lib.models.user.Role
import io.github.juevigrace.diva.lib.models.user.permissions.UserPermission
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserPermissionsStorageImpl(
    private val db: DivaDatabase<DivaDB>
) : UserPermissionsStorage {

    override suspend fun getAllByUser(userId: Uuid): Result<List<UserPermission>> {
        return db.getList {
            userPermissionsQueries.findAllByUser(userId.toString(), ::mapToUserPermission)
        }
    }

    override fun getAllByUserFlow(userId: Uuid): Flow<Result<List<UserPermission>>> {
        return db.getListAsFlow {
            userPermissionsQueries.findAllByUser(userId.toString(), ::mapToUserPermission)
        }
    }

    override suspend fun getById(permissionId: Uuid, userId: Uuid): Result<Option<UserPermission>> {
        return db.getOne {
            userPermissionsQueries.findOneById(permissionId.toString(), userId.toString(), ::mapToUserPermission)
        }
    }

    override fun getByIdFlow(permissionId: Uuid, userId: Uuid): Flow<Result<Option<UserPermission>>> {
        return db.getOneAsFlow {
            userPermissionsQueries.findOneById(permissionId.toString(), userId.toString(), ::mapToUserPermission)
        }
    }

    override suspend fun upsert(item: UserPermission): Result<Unit> {
        return db.use {
            transaction {
                userPermissionsQueries.upsert(
                    permission_id = item.permission.id.toString(),
                    user_id = item.userId.toString(),
                    granted_by = item.grantedBy.getOrNull(),
                    granted = item.granted,
                    granted_at = item.grantedAt.map { it.epochSeconds }.getOrNull() ?: 0L,
                    expires_at = item.expiresAt.map { it.epochSeconds }.getOrNull(),
                    updated_at = item.updatedAt.epochSeconds
                )
            }
        }
    }

    override suspend fun delete(permissionId: Uuid, userId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                userPermissionsQueries.deleteById(permissionId.toString(), userId.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                userPermissionsQueries.deleteAll()
            }
        }
    }

    private fun mapToUserPermission(
        permissionId: String,
        userId: String,
        grantedBy: String?,
        granted: Boolean,
        grantedAt: Long,
        expiresAt: Long?,
        updatedAt: Long,
    ): UserPermission = UserPermission(
        permission = Permission(
            id = Uuid.parse(permissionId),
            name = "",
            description = "",
            action = PermissionAction.PERMISSION_NONE,
            roleLevel = Role.USER,
            createdAt = Instant.fromEpochSeconds(0),
            updatedAt = Instant.fromEpochSeconds(0)
        ),
        userId = Uuid.parse(userId),
        grantedBy = grantedBy.toOption(),
        granted = granted,
        grantedAt = Instant.fromEpochSeconds(grantedAt).toOption(),
        expiresAt = expiresAt?.let { Instant.fromEpochSeconds(it) }.toOption(),
        updatedAt = Instant.fromEpochSeconds(updatedAt)
    )
}
