@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.auth.api.signup

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.session.api.SessionDataDto
import io.github.juevigrace.diva.lib.models.user.api.CreateUserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport

@Serializable
data class SignUpDto(
    @SerialName("user")
    val user: CreateUserDto,
    @SerialName("session_data")
    val sessionData: SessionDataDto,
)
