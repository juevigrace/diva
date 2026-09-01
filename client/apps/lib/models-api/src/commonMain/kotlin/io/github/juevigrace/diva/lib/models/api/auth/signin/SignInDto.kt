package io.github.juevigrace.diva.lib.models.api.auth.signin

import io.github.juevigrace.diva.lib.models.api.auth.session.SessionDataDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInDto(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
    @SerialName("session_data")
    val sessionData: SessionDataDto,
)
