@file:DivaJsExport

package io.github.juevigrace.diva.lib.auth.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordDto(
    @SerialName("new_password")
    val newPassword: String,
)
