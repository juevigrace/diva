package com.diva.chat.api.handler

import com.diva.chat.data.ChatService
import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.chat.dtos.CreateChatDto
import com.diva.models.api.chat.dtos.UpdateChatDto
import com.diva.models.api.chat.response.ChatResponse
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface ChatHandler {
    suspend fun getChats(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<ChatResponse>>, DivaError>
    suspend fun getChat(id: String): DivaResult<ApiResponse<ChatResponse>, DivaError>
    suspend fun createChat(dto: CreateChatDto): DivaResult<ApiResponse<ChatResponse>, DivaError>
    suspend fun updateChat(dto: UpdateChatDto): DivaResult<ApiResponse<ChatResponse>, DivaError>
    suspend fun deleteChat(id: String): DivaResult<ApiResponse<Unit>, DivaError>
}

class ChatHandlerImpl(
    private val service: ChatService
) : ChatHandler {
    override suspend fun getChats(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<ChatResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun getChat(id: String): DivaResult<ApiResponse<ChatResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createChat(dto: CreateChatDto): DivaResult<ApiResponse<ChatResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun updateChat(dto: UpdateChatDto): DivaResult<ApiResponse<ChatResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChat(id: String): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
