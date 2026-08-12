package com.diva.models.api.user.state.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
