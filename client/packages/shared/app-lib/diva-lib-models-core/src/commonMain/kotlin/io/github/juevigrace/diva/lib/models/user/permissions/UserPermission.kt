package io.github.juevigrace.diva.lib.models.user.permissions

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.api.user.permissions.UserPermissionResponse
import io.github.juevigrace.diva.lib.models.permission.Permission
import io.github.juevigrace.diva.lib.models.permission.PermissionAction
import io.github.juevigrace.diva.lib.models.user.Role
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalJsExport::class)
@JsExport
data class UserPermission(
    val permission: Permission,
    val userId: Uuid,
    val grantedBy: Option<String> = Option.None,
    val granted: Boolean,
    val grantedAt: Option<Instant> = Option.None,
    val expiresAt: Option<Instant> = Option.None,
    val updatedAt: Instant,
) {
    companion object {
        fun fromResponse(response: UserPermissionResponse): UserPermission {
            return UserPermission(
                permission = Permission(
                    id = Uuid.parse(response.permissionId),
                    name = "",
                    description = "",
                    action = PermissionAction.PERMISSION_NONE,
                    roleLevel = Role.USER,
                    createdAt = Instant.fromEpochMilliseconds(response.grantedAt ?: response.updatedAt),
                    updatedAt = Instant.fromEpochMilliseconds(response.updatedAt),
                ),
                userId = Uuid.NIL,
                grantedBy = Option.of(response.grantedBy),
                granted = response.granted,
                grantedAt = Option.of(response.grantedAt?.let { Instant.fromEpochMilliseconds(it) }),
                expiresAt = Option.of(response.expiresAt?.let { Instant.fromEpochMilliseconds(it) }),
                updatedAt = Instant.fromEpochMilliseconds(response.updatedAt),
            )
        }
    }
}
