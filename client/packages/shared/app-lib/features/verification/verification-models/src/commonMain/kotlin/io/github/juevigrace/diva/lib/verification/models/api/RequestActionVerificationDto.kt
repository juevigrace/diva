@file:DivaJsExport

package io.github.juevigrace.diva.lib.verification.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestActionVerificationDto(
    @SerialName("email")
    val email: String,
    @SerialName("action")
    val action: String,
)
