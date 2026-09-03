package io.github.juevigrace.diva.lib.models.api.auth.signup

import io.github.juevigrace.diva.lib.models.api.auth.session.SessionDataDto
import io.github.juevigrace.diva.lib.models.api.user.CreateUserDto
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class SignUpDto(
    @SerialName("user")
    val user: CreateUserDto,
    @SerialName("session_data")
    val sessionData: SessionDataDto,
)
