@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.api.auth.forgot.password

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.api.auth.session.SessionDataDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport

@Serializable
data class ForgotPasswordConfirmDto(
    @SerialName("id")
    val id: String,
    @SerialName("session_data")
    val sessionData: SessionDataDto,
)
