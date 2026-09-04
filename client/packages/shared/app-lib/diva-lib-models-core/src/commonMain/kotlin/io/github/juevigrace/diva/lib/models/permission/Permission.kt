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
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Permission(
    val id: Uuid,
    val name: String,
    val description: String,
    val action: PermissionAction,
    val roleLevel: Role,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Option<Instant> = None,
) {
    companion object {
        fun fromResponse(response: PermissionResponse): Permission {
            return Permission(
                id = Uuid.parse(response.id),
                name = response.name,
                description = response.description,
                action = safePermissionAction(response.action),
                roleLevel = safeRole(response.roleLevel),
                createdAt = Instant.fromEpochMilliseconds(response.createdAt),
                updatedAt = Instant.fromEpochMilliseconds(response.updatedAt),
            )
        }
    }
}
