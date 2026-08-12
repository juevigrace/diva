package com.diva.media.api.validation

import com.diva.models.api.media.dtos.CreateMediaDto
import com.diva.models.api.media.dtos.CreateTagDto
import com.diva.models.api.media.dtos.DeleteMediaTagDto
import com.diva.models.api.media.dtos.UpdateMediaDto
import com.diva.models.api.media.dtos.UpdateTagDto
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun RequestValidationConfig.mediaRequestValidation() {
    validate<CreateMediaDto> { dto ->
        when {
            dto.url.isBlank() -> ValidationResult.Invalid("URL cannot be blank")
            dto.mediaType.isBlank() -> ValidationResult.Invalid("Media type cannot be blank")
            dto.visibility.isBlank() -> ValidationResult.Invalid("Visibility cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<UpdateMediaDto> { dto ->
        when {
            dto.id.isBlank() -> ValidationResult.Invalid("Media ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<DeleteMediaTagDto> { dto ->
        when {
            dto.tagId.isBlank() -> ValidationResult.Invalid("Tag ID cannot be blank")
            dto.mediaId.isBlank() -> ValidationResult.Invalid("Media ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<CreateTagDto> { dto ->
        when {
            dto.tagName.isBlank() -> ValidationResult.Invalid("Tag name cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<UpdateTagDto> { dto ->
        when {
            dto.id.isBlank() -> ValidationResult.Invalid("Tag ID cannot be blank")
            dto.tagName.isBlank() -> ValidationResult.Invalid("Tag name cannot be blank")
            else -> ValidationResult.Valid
        }
    }
}
