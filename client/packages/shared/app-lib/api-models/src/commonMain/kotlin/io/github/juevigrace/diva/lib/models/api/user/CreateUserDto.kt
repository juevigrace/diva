@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.api.user

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport

@Serializable
data class CreateUserDto(
    @SerialName("email")
    val email: String,
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
)
