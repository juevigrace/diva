package io.github.juevigrace.diva.lib.models.api.user.state

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
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
