package io.github.juevigrace.diva.lib.user.database.permissions

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.user.DivaSharedDB
import io.github.juevigrace.diva.lib.models.permission.Permission
import io.github.juevigrace.diva.lib.models.permission.PermissionAction
import io.github.juevigrace.diva.lib.models.user.Role
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

class UserPermissionsStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>
) : UserPermissionsStorage {

    override suspend fun findAll(userId: String): Result<List<UserPermission>> {
        return db.getList {
            userPermissionsQueries.findAll(userId, ::mapToUserPermission)
        }
    }

    override fun findAllFlow(userId: String): Flow<Result<List<UserPermission>>> {
        return db.getListAsFlow {
            userPermissionsQueries.findAll(userId, ::mapToUserPermission)
        }
    }

    override suspend fun findOne(permissionId: String, userId: String): Result<Option<UserPermission>> {
        return db.getOne {
            userPermissionsQueries.findOne(permissionId, userId, ::mapToUserPermission)
        }
    }

    override fun findOneFlow(permissionId: String, userId: String): Flow<Result<Option<UserPermission>>> {
        return db.getOneAsFlow {
            userPermissionsQueries.findOne(permissionId, userId, ::mapToUserPermission)
        }
    }

    override suspend fun upsert(userId: String, item: UserPermission): Result<Unit> {
        return db.use {
            transaction {
                userPermissionsQueries.upsert(
                    permission_id = item.permission.id,
                    user_id = userId,
                    granted_by = item.grantedBy.getOrNull(),
                    granted = item.granted,
                    granted_at = item.grantedAt.getOrNull() ?: 0L,
                    expires_at = item.expiresAt.getOrNull(),
                    updated_at = item.updatedAt
                )
            }
        }
    }

    override suspend fun deleteOne(permissionId: String, userId: String): Result<Unit> {
        return db.use {
            transaction {
                userPermissionsQueries.deleteOne(permissionId, userId)
            }
        }
    }

    override suspend fun delete(): Result<Unit> {
        return db.use {
            transaction {
                userPermissionsQueries.delete()
            }
        }
    }

    @Suppress("LongParameterList")
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
            id = permissionId,
            name = "",
            description = "",
            action = PermissionAction.PERMISSION_NONE,
            roleLevel = Role.USER,
            createdAt = 0L,
            updatedAt = 0L
        ),
        grantedBy = grantedBy.toOption(),
        granted = granted,
        grantedAt = grantedAt.toOption(),
        expiresAt = expiresAt.toOption(),
        updatedAt = updatedAt
    )
}
