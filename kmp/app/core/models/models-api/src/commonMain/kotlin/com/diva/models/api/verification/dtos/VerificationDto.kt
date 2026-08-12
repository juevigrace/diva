package com.diva.models.api.verification.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerificationDto(
    @SerialName("action_id")
    val actionId: String = "",
    @SerialName("token")
    val token: String,
)
