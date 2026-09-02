package io.github.juevigrace.diva.lib.models.api.verification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestActionVerificationDto(
    @SerialName("email")
    val email: String,
    @SerialName("action")
    val action: String,
)
