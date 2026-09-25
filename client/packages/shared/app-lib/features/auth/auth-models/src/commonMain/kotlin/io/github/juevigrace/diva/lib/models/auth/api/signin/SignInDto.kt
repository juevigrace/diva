@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.auth.api.signin

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.session.api.SessionDataDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport

@Serializable
data class SignInDto(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
    @SerialName("session_data")
    val sessionData: SessionDataDto,
)
