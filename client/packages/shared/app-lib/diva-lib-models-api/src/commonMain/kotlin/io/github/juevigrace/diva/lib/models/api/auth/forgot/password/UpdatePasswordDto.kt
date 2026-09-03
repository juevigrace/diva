package io.github.juevigrace.diva.lib.models.api.auth.forgot.password

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class UpdatePasswordDto(
    @SerialName("new_password")
    val newPassword: String,
)
