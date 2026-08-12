package com.diva.chat.api.handler

import com.diva.chat.data.ChatMessageService
import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.chat.dtos.CreateMessageDto
import com.diva.models.api.chat.dtos.DeleteMessageDto
import com.diva.models.api.chat.dtos.UpdateMessageDto
import com.diva.models.api.chat.response.MessageResponse
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface ChatMessageHandler {
    suspend fun getChatMessages(
        chatId: String,
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<MessageResponse>>, DivaError>
    suspend fun createChatMessage(dto: CreateMessageDto): DivaResult<ApiResponse<MessageResponse>, DivaError>
    suspend fun updateChatMessage(dto: UpdateMessageDto): DivaResult<ApiResponse<MessageResponse>, DivaError>
    suspend fun deleteChatMessage(dto: DeleteMessageDto): DivaResult<ApiResponse<Unit>, DivaError>
}

class ChatMessageHandlerImpl(
    private val service: ChatMessageService
) : ChatMessageHandler {
    override suspend fun getChatMessages(
        chatId: String,
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<MessageResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createChatMessage(dto: CreateMessageDto): DivaResult<ApiResponse<MessageResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun updateChatMessage(dto: UpdateMessageDto): DivaResult<ApiResponse<MessageResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChatMessage(dto: DeleteMessageDto): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
