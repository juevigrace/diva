@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.auth

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport

data class PasswordResetForm(
    val newPassword: String = "",
    val confirmPassword: String = "",
)
