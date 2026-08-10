package com.diva.collection.api.validation

import com.diva.models.api.collection.dtos.CreateCollectionDto
import com.diva.models.api.collection.dtos.CreatePlaylistSuggestionDto
import com.diva.models.api.collection.dtos.PlaylistContributorDto
import com.diva.models.api.collection.dtos.PlaylistDto
import com.diva.models.api.collection.dtos.UpdateCollectionDto
import com.diva.models.api.collection.dtos.UpdatePlaylistSuggestionDto
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun RequestValidationConfig.collectionRequestValidation() {
    validate<CreateCollectionDto> { dto ->
        when {
            dto.name.isBlank() -> ValidationResult.Invalid("Collection name cannot be blank")
            dto.collectionType.isBlank() -> ValidationResult.Invalid("Collection type cannot be blank")
            dto.visibility.isBlank() -> ValidationResult.Invalid("Visibility cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<UpdateCollectionDto> { dto ->
        when {
            dto.collectionId.isBlank() -> ValidationResult.Invalid("Collection ID cannot be blank")
            dto.name.isBlank() -> ValidationResult.Invalid("Collection name cannot be blank")
            dto.visibility.isBlank() -> ValidationResult.Invalid("Visibility cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<PlaylistDto> { dto ->
        when {
            dto.collectionId.isBlank() -> ValidationResult.Invalid("Collection ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<PlaylistContributorDto> { dto ->
        when {
            dto.collectionId.isBlank() -> ValidationResult.Invalid("Collection ID cannot be blank")
            dto.contributorId.isBlank() -> ValidationResult.Invalid("Contributor ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<CreatePlaylistSuggestionDto> { dto ->
        when {
            dto.collectionId.isBlank() -> ValidationResult.Invalid("Collection ID cannot be blank")
            dto.mediaId.isBlank() -> ValidationResult.Invalid("Media ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<UpdatePlaylistSuggestionDto> { dto ->
        when {
            dto.id.isBlank() -> ValidationResult.Invalid("Suggestion ID cannot be blank")
            dto.status.isBlank() -> ValidationResult.Invalid("Status cannot be blank")
            else -> ValidationResult.Valid
        }
    }
}
