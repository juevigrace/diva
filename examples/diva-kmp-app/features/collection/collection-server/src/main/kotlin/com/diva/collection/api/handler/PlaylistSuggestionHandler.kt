package com.diva.collection.api.handler

import com.diva.collection.data.playlist.PlaylistSuggestionService
import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.collection.dtos.CreatePlaylistSuggestionDto
import com.diva.models.api.collection.dtos.UpdatePlaylistSuggestionDto
import com.diva.models.api.collection.response.PlaylistSuggestionResponse
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface PlaylistSuggestionHandler {
    suspend fun getPlaylistSuggestions(
        playlistId: String,
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<PlaylistSuggestionResponse>>, DivaError>
    suspend fun createPlaylistSuggestion(dto: CreatePlaylistSuggestionDto): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun updatePlaylistSuggestion(
        dto: UpdatePlaylistSuggestionDto
    ): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun deletePlaylistSuggestion(id: String): DivaResult<ApiResponse<Unit>, DivaError>
}

class PlaylistSuggestionHandlerImpl(
    private val service: PlaylistSuggestionService
) : PlaylistSuggestionHandler {
    override suspend fun getPlaylistSuggestions(
        playlistId: String,
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<PlaylistSuggestionResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createPlaylistSuggestion(
        dto: CreatePlaylistSuggestionDto
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlaylistSuggestion(
        dto: UpdatePlaylistSuggestionDto
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlaylistSuggestion(id: String): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
