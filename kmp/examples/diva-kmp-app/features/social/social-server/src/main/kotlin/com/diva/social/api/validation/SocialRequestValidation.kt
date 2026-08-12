package com.diva.social.api.validation

import com.diva.models.api.social.interaction.comment.dtos.CreateCommentDto
import com.diva.models.api.social.interaction.comment.dtos.UpdateCommentDto
import com.diva.models.api.social.interaction.dtos.CreateInteractionDto
import com.diva.models.api.social.interaction.share.dtos.CreateShareDto
import com.diva.models.api.social.post.dtos.CreatePostDto
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun RequestValidationConfig.socialRequestValidation() {
    validate<CreatePostDto> { dto ->
        when {
            dto.text.isBlank() -> ValidationResult.Invalid("Post text cannot be blank")
            dto.visibility.isBlank() -> ValidationResult.Invalid("Visibility cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<CreateCommentDto> { dto ->
        when {
            dto.interactionId.isBlank() -> ValidationResult.Invalid("Interaction ID cannot be blank")
            dto.content.isBlank() -> ValidationResult.Invalid("Content cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<UpdateCommentDto> { dto ->
        when {
            dto.interactionId.isBlank() -> ValidationResult.Invalid("Interaction ID cannot be blank")
            dto.content.isBlank() -> ValidationResult.Invalid("Content cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<CreateInteractionDto> { dto ->
        when {
            dto.postId.isBlank() -> ValidationResult.Invalid("Post ID cannot be blank")
            dto.reactionType.isBlank() -> ValidationResult.Invalid("Reaction type cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<CreateShareDto> { dto ->
        when {
            dto.interactionId.isBlank() -> ValidationResult.Invalid("Interaction ID cannot be blank")
            dto.shareType.isBlank() -> ValidationResult.Invalid("Share type cannot be blank")
            else -> ValidationResult.Valid
        }
    }
}
