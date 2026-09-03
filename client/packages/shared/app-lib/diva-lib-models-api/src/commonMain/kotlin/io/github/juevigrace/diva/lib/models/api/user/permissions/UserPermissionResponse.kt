package io.github.juevigrace.diva.lib.models.api.user.permissions

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
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
