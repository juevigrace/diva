package com.diva.verification.api.validation

import com.diva.models.api.user.dtos.EmailTokenDto
import com.diva.util.isValidEmail
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun RequestValidationConfig.verificationRequestValidation() {
    validate<EmailTokenDto> { dto ->
        when {
            dto.token.isBlank() -> ValidationResult.Invalid("Token cannot be blank")
            dto.token.length != 6 -> ValidationResult.Invalid("Token must be 6 digits")
            !dto.token.all { it.isDigit() } -> ValidationResult.Invalid("Token must be numeric")
            else -> ValidationResult.Valid
        }
    }
}
