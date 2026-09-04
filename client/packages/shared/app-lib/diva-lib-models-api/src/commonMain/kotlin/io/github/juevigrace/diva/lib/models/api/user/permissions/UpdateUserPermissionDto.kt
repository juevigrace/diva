@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.api.user.permissions

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport

@Serializable
data class UpdateUserPermissionDto(
    @SerialName("granted")
    val granted: Boolean,
    @SerialName("expires_at")
    val expiresAt: Long? = null,
)
