package com.diva.chat.api.handler

import com.diva.chat.data.ChatParticipantService
import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.chat.dtos.AddParticipantDto
import com.diva.models.api.chat.dtos.DeleteParticipantDto
import com.diva.models.api.chat.dtos.UpdateParticipantDto
import com.diva.models.api.chat.response.ChatParticipantResponse
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError
import kotlin.uuid.ExperimentalUuidApi

interface ChatParticipantHandler {

    suspend fun getChatParticipants(
        chatId: String,
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<ChatParticipantResponse>>, DivaError>

    suspend fun createChatParticipant(
        dto: AddParticipantDto
    ): DivaResult<ApiResponse<ChatParticipantResponse>, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun updateChatParticipant(
        dto: UpdateParticipantDto
    ): DivaResult<ApiResponse<ChatParticipantResponse>, DivaError>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun deleteChatParticipant(dto: DeleteParticipantDto): DivaResult<ApiResponse<Unit>, DivaError>
}

class ChatParticipantHandlerImpl(
    private val service: ChatParticipantService
) : ChatParticipantHandler {
    override suspend fun getChatParticipants(
        chatId: String,
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<ChatParticipantResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createChatParticipant(dto: AddParticipantDto): DivaResult<ApiResponse<ChatParticipantResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun updateChatParticipant(dto: UpdateParticipantDto): DivaResult<ApiResponse<ChatParticipantResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChatParticipant(dto: DeleteParticipantDto): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
