@file:DivaJsExport

package io.github.juevigrace.diva.lib.auth.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.session.models.api.SessionDataDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordConfirmDto(
    @SerialName("id")
    val id: String,
    @SerialName("session_data")
    val sessionData: SessionDataDto,
)
