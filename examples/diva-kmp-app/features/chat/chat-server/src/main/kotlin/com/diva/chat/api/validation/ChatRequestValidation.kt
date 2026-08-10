package com.diva.chat.api.validation

import com.diva.models.api.chat.dtos.AddParticipantDto
import com.diva.models.api.chat.dtos.CreateChatDto
import com.diva.models.api.chat.dtos.CreateMessageDto
import com.diva.models.api.chat.dtos.DeleteParticipantDto
import com.diva.models.api.chat.dtos.DeleteMessageDto
import com.diva.models.api.chat.dtos.UpdateChatDto
import com.diva.models.api.chat.dtos.UpdateMessageDto
import com.diva.models.api.chat.dtos.UpdateParticipantDto
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun RequestValidationConfig.chatRequestValidation() {
    validate<CreateChatDto> { dto ->
        when {
            dto.type.isBlank() -> ValidationResult.Invalid("Chat type cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<UpdateChatDto> { dto ->
        when {
            dto.id.isBlank() -> ValidationResult.Invalid("Chat ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<CreateMessageDto> { dto ->
        when {
            dto.chatId.isBlank() -> ValidationResult.Invalid("Chat ID cannot be blank")
            dto.content.isBlank() -> ValidationResult.Invalid("Content cannot be blank")
            dto.type.isBlank() -> ValidationResult.Invalid("Message type cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<UpdateMessageDto> { dto ->
        when {
            dto.messageId.isBlank() -> ValidationResult.Invalid("Message ID cannot be blank")
            dto.chatId.isBlank() -> ValidationResult.Invalid("Chat ID cannot be blank")
            dto.content.isBlank() -> ValidationResult.Invalid("Content cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<DeleteMessageDto> { dto ->
        when {
            dto.messageId.isBlank() -> ValidationResult.Invalid("Message ID cannot be blank")
            dto.chatId.isBlank() -> ValidationResult.Invalid("Chat ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<AddParticipantDto> { dto ->
        when {
            dto.chatId.isBlank() -> ValidationResult.Invalid("Chat ID cannot be blank")
            dto.userId.isBlank() -> ValidationResult.Invalid("User ID cannot be blank")
            dto.role.isBlank() -> ValidationResult.Invalid("Role cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<UpdateParticipantDto> { dto ->
        when {
            dto.chatId.isBlank() -> ValidationResult.Invalid("Chat ID cannot be blank")
            dto.userId.isBlank() -> ValidationResult.Invalid("User ID cannot be blank")
            dto.role.isBlank() -> ValidationResult.Invalid("Role cannot be blank")
            else -> ValidationResult.Valid
        }
    }
    validate<DeleteParticipantDto> { dto ->
        when {
            dto.chatId.isBlank() -> ValidationResult.Invalid("Chat ID cannot be blank")
            dto.userId.isBlank() -> ValidationResult.Invalid("User ID cannot be blank")
            else -> ValidationResult.Valid
        }
    }
}
