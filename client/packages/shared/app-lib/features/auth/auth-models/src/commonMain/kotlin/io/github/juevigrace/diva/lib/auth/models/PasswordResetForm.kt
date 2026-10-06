@file:DivaJsExport

package io.github.juevigrace.diva.lib.auth.models

import io.github.juevigrace.diva.core.DivaJsExport

data class PasswordResetForm(
    val newPassword: String = "",
    val confirmPassword: String = "",
)
