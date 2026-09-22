@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.user.permissions

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.api.user.permissions.UserPermissionResponse
import io.github.juevigrace.diva.lib.models.permission.Permission
import io.github.juevigrace.diva.lib.models.permission.PermissionAction
import io.github.juevigrace.diva.lib.models.user.Role
import kotlin.js.ExperimentalJsExport

data class UserPermission(
    val permission: Permission,
    val grantedBy: Option<String> = None,
    val granted: Boolean,
    val grantedAt: Option<Long> = None,
    val expiresAt: Option<Long> = None,
    val updatedAt: Long,
) {
    companion object {
        fun fromResponse(response: UserPermissionResponse): UserPermission {
            return UserPermission(
                permission = Permission(
                    id = response.permissionId,
                    name = "",
                    description = "",
                    action = PermissionAction.PERMISSION_NONE,
                    roleLevel = Role.USER,
                    createdAt = response.grantedAt ?: response.updatedAt,
                    updatedAt = response.updatedAt,
                ),
                grantedBy = Option.of(response.grantedBy),
                granted = response.granted,
                grantedAt = Option.of(response.grantedAt),
                expiresAt = Option.of(response.expiresAt),
                updatedAt = response.updatedAt,
            )
        }
    }
}
