@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.api.user.permissions

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport

@Serializable
data class UserPermissionResponse(
    @SerialName("permission_id")
    val permissionId: String,
    @SerialName("granted_by")
    val grantedBy: String? = null,
    @SerialName("granted")
    val granted: Boolean,
    @SerialName("granted_at")
    val grantedAt: Long? = null,
    @SerialName("expires_at")
    val expiresAt: Long? = null,
    @SerialName("updated_at")
    val updatedAt: Long,
)
