@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.api.user.state

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport

@Serializable
data class UserStateResponse(
    @SerialName("verified")
    val verified: Boolean,
    @SerialName("status")
    val status: String,
    @SerialName("last_active_at")
    val lastActiveAt: Long,
    @SerialName("updated_at")
    val updatedAt: Long,
)
