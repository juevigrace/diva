package io.github.juevigrace.diva.lib.models.auth

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class PasswordResetForm(
    val newPassword: String = "",
    val confirmPassword: String = "",
)
