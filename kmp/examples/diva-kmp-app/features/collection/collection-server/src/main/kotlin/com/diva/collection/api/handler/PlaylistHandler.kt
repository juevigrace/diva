package com.diva.collection.api.handler

import com.diva.collection.data.playlist.PlaylistService
import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.collection.dtos.PlaylistDto
import com.diva.models.api.collection.response.PlaylistResponse
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface PlaylistHandler {
    suspend fun getPlaylists(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<PlaylistResponse>>, DivaError>
    suspend fun getPlaylist(id: String): DivaResult<ApiResponse<PlaylistResponse>, DivaError>
    suspend fun createPlaylist(dto: PlaylistDto): DivaResult<ApiResponse<PlaylistResponse>, DivaError>
    suspend fun updatePlaylist(
        dto: PlaylistDto
    ): DivaResult<ApiResponse<PlaylistResponse>, DivaError>
    suspend fun deletePlaylist(id: String): DivaResult<ApiResponse<Unit>, DivaError>
}

class PlaylistHandlerImpl(
    private val service: PlaylistService
) : PlaylistHandler {
    override suspend fun getPlaylists(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<PlaylistResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlaylist(id: String): DivaResult<ApiResponse<PlaylistResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createPlaylist(dto: PlaylistDto): DivaResult<ApiResponse<PlaylistResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlaylist(dto: PlaylistDto): DivaResult<ApiResponse<PlaylistResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlaylist(id: String): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
