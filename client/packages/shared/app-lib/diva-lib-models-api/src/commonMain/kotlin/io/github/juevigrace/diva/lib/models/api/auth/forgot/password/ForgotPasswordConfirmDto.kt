package io.github.juevigrace.diva.lib.models.api.auth.forgot.password

import io.github.juevigrace.diva.lib.models.api.auth.session.SessionDataDto
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class ForgotPasswordConfirmDto(
    @SerialName("id")
    val id: String,
    @SerialName("session_data")
    val sessionData: SessionDataDto,
)
