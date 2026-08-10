package com.diva.permissions.api.validation

import com.diva.models.api.permissions.dtos.CreatePermissionDto
import com.diva.models.api.permissions.dtos.UpdatePermissionDto
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun RequestValidationConfig.permissionsRequestValidation() {
    validate<CreatePermissionDto> { dto ->
        when {
            dto.name.isBlank() -> ValidationResult.Invalid("Permission name cannot be blank")
            dto.roleLevel.isBlank() -> ValidationResult.Invalid("Role level cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<UpdatePermissionDto> { dto ->
        when {
            dto.name.isBlank() -> ValidationResult.Invalid("Permission name cannot be blank")
            else -> ValidationResult.Valid
        }
    }
}
