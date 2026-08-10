package com.diva.collection.api.handler

import com.diva.collection.data.playlist.PlaylistContributorService
import com.diva.models.api.ApiResponse
import com.diva.models.api.collection.dtos.PlaylistContributorDto
import com.diva.models.api.collection.response.PlaylistContributorResponse
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface PlaylistContributorHandler {
    suspend fun getContributors(
        playlistId: String
    ): DivaResult<ApiResponse<List<PlaylistContributorResponse>>, DivaError>
    suspend fun createPlaylistContributor(dto: PlaylistContributorDto): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun deletePlaylistContributor(dto: PlaylistContributorDto): DivaResult<ApiResponse<Unit>, DivaError>
}

class PlaylistContributorHandlerImpl(
    private val service: PlaylistContributorService
) : PlaylistContributorHandler {
    override suspend fun getContributors(
        playlistId: String
    ): DivaResult<ApiResponse<List<PlaylistContributorResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createPlaylistContributor(
        dto: PlaylistContributorDto
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlaylistContributor(
        dto: PlaylistContributorDto
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
