package io.github.juevigrace.diva.lib.models.api.auth.forgot.password

import io.github.juevigrace.diva.lib.models.api.auth.session.SessionDataDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordConfirmDto(
    @SerialName("id")
    val id: String,
    @SerialName("session_data")
    val sessionData: SessionDataDto,
)
