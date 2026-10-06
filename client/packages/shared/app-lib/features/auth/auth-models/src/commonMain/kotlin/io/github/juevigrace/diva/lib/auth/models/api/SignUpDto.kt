@file:DivaJsExport

package io.github.juevigrace.diva.lib.auth.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.session.models.api.SessionDataDto
import io.github.juevigrace.diva.lib.user.models.api.CreateUserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpDto(
    @SerialName("user")
    val user: CreateUserDto,
    @SerialName("session_data")
    val sessionData: SessionDataDto,
)
