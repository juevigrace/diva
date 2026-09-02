package io.github.juevigrace.diva.lib.models.api.user.permissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserPermissionDto(
    @SerialName("permission_action")
    val permissionAction: String,
    @SerialName("granted")
    val granted: Boolean,
    @SerialName("expires_at")
    val expiresAt: Long? = null,
)
