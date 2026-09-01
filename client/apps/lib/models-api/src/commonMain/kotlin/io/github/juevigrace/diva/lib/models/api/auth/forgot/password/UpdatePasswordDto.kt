package io.github.juevigrace.diva.lib.models.api.auth.forgot.password

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordDto(
    @SerialName("new_password")
    val newPassword: String,
)
