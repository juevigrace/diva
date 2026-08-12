package com.diva.auth.api.validation

import com.diva.models.api.auth.dtos.SignInDto
import com.diva.models.api.auth.dtos.SignUpDto
import com.diva.util.isValidEmail
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun RequestValidationConfig.authRequestValidation() {
    validate<SignInDto> { dto ->
        when {
            dto.username.isBlank() -> ValidationResult.Invalid("Username cannot be blank")
            dto.password.isBlank() -> ValidationResult.Invalid("Password cannot be blank")
            dto.password.length < 4 -> ValidationResult.Invalid("Password must be at least 4 characters long")
            dto.sessionData.device.isBlank() -> ValidationResult.Invalid("Device ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<SignUpDto> { dto ->
        when {
            dto.user.username.isBlank() -> ValidationResult.Invalid("Username cannot be blank")
            dto.user.password.isBlank() -> ValidationResult.Invalid("Password cannot be blank")
            dto.user.password.length < 4 -> ValidationResult.Invalid("Password must be at least 4 characters long")
            dto.user.email.isBlank() -> ValidationResult.Invalid("Email cannot be blank")
            !isValidEmail(dto.user.email) -> ValidationResult.Invalid("Email format is invalid")
            dto.user.birthDate == 0L -> ValidationResult.Invalid("Birth date cannot be blank")
            dto.user.bio.length > 200 -> ValidationResult.Invalid("Bio cannot be longer than 200 characters")
            dto.sessionData.device.isBlank() -> ValidationResult.Invalid("Device ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
}
