package com.diva.auth.presentation.forgot.state

data class ForgotState(
    val email: String = "",
    val token: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val currentStep: ForgotStep = ForgotStep.EMAIL_INPUT,
    val loading: Boolean = false,
    val emailError: String? = null,
    val tokenError: String? = null,
    val passwordError: String? = null,
    val success: Boolean = false,
)

enum class ForgotStep {
    EMAIL_INPUT,
    TOKEN_INPUT,
    NEW_PASSWORD,
}
