@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.permission

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.api.permission.PermissionResponse
import io.github.juevigrace.diva.lib.models.user.Role
import io.github.juevigrace.diva.lib.models.user.safeRole
import kotlin.js.ExperimentalJsExport

data class Permission(
    val id: String,
    val name: String,
    val description: String,
    val action: PermissionAction,
    val roleLevel: Role,
    val createdAt: Long,
    val updatedAt: Long,
    val deletedAt: Option<Long> = None,
) {
    companion object {
        fun fromResponse(response: PermissionResponse): Permission {
            return Permission(
                id = response.id,
                name = response.name,
                description = response.description,
                action = safePermissionAction(response.action),
                roleLevel = safeRole(response.roleLevel),
                createdAt = response.createdAt,
                updatedAt = response.updatedAt,
            )
        }
    }
}
