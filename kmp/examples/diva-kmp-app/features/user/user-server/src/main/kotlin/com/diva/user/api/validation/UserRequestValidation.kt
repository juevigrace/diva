package com.diva.user.api.validation

import com.diva.models.api.user.dtos.CreateUserDto
import com.diva.models.api.user.dtos.UpdateUserDto
import com.diva.models.api.user.permissions.dtos.DeleteUserPermissionDto
import com.diva.models.api.user.permissions.dtos.UserPermissionDto
import com.diva.util.isValidEmail
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun RequestValidationConfig.userRequestValidation() {
    validate<CreateUserDto> { dto ->
        when {
            dto.email.isBlank() -> ValidationResult.Invalid("Email cannot be blank")
            !isValidEmail(dto.email) -> ValidationResult.Invalid("Email format is invalid")
            dto.username.isBlank() -> ValidationResult.Invalid("Username cannot be blank")
            dto.password.isBlank() -> ValidationResult.Invalid("Password cannot be blank")
            dto.birthDate == 0L -> ValidationResult.Invalid("Birth date cannot be blank")
            dto.phoneNumber.isBlank() -> ValidationResult.Invalid("Phone number cannot be blank")
            dto.bio.length > 200 -> ValidationResult.Invalid("Bio cannot be longer than 200 characters")
            else -> ValidationResult.Valid
        }
    }
    validate<UpdateUserDto> { dto ->
        when {
            dto.username.isBlank() -> ValidationResult.Invalid("Username cannot be blank")
            dto.bio.length > 200 -> ValidationResult.Invalid("Bio cannot be longer than 200 characters")
            else -> ValidationResult.Valid
        }
    }
    validate<UserPermissionDto> { dto ->
        when {
            dto.userId.isBlank() -> ValidationResult.Invalid("User ID cannot be blank")
            dto.permissionId.isBlank() -> ValidationResult.Invalid("Permission ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<DeleteUserPermissionDto> { dto ->
        when {
            dto.userId.isBlank() -> ValidationResult.Invalid("User ID cannot be blank")
            dto.permissionId.isBlank() -> ValidationResult.Invalid("Permission ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
}
