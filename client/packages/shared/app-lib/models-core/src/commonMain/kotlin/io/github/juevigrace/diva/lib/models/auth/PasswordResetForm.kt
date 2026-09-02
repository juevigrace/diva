package io.github.juevigrace.diva.lib.models.auth

data class PasswordResetForm(
    val newPassword: String = "",
    val confirmPassword: String = "",
)
