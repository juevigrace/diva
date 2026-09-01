package io.github.juevigrace.diva.lib.models.api.verification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyActionDto(
    @SerialName("action_id")
    val actionId: String,
    @SerialName("token")
    val token: String,
)
