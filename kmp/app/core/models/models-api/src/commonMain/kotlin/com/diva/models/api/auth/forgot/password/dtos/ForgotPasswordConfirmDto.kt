package com.diva.models.api.auth.forgot.password.dtos

import com.diva.models.api.auth.session.dtos.SessionDataDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordConfirmDto(
    @SerialName("id")
    val id: String,
    @SerialName("session_data")
    val sessionData: SessionDataDto,
)
