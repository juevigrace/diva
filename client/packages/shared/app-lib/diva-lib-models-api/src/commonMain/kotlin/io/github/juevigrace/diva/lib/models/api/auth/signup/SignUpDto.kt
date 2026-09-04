@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.api.auth.signup

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.api.auth.session.SessionDataDto
import io.github.juevigrace.diva.lib.models.api.user.CreateUserDto
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
