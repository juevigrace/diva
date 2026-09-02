package io.github.juevigrace.diva.lib.models.api.user.permissions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserPermissionDto(
    @SerialName("granted")
    val granted: Boolean,
    @SerialName("expires_at")
    val expiresAt: Long? = null,
)
