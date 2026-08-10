package com.diva.media.api.handler

import com.diva.media.data.MediaService
import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.media.dtos.CreateMediaDto
import com.diva.models.api.media.dtos.DeleteMediaTagDto
import com.diva.models.api.media.dtos.UpdateMediaDto
import com.diva.models.api.media.response.MediaResponse
import com.diva.models.api.media.response.MediaTagResponse
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface MediaHandler {
    suspend fun getMedia(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<MediaResponse>>, DivaError>
    suspend fun getMedia(id: String): DivaResult<ApiResponse<MediaResponse>, DivaError>
    suspend fun getMediaTagsByMediaId(mediaId: String): DivaResult<ApiResponse<List<MediaTagResponse>>, DivaError>
    suspend fun createMedia(dto: CreateMediaDto): DivaResult<ApiResponse<MediaResponse>, DivaError>
    suspend fun updateMedia(dto: UpdateMediaDto): DivaResult<ApiResponse<MediaResponse>, DivaError>
    suspend fun deleteMedia(id: String): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun deleteMediaTag(dto: DeleteMediaTagDto): DivaResult<ApiResponse<Unit>, DivaError>
}

class MediaHandlerImpl(
    private val service: MediaService
) : MediaHandler {
    override suspend fun getMedia(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<MediaResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun getMedia(id: String): DivaResult<ApiResponse<MediaResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun getMediaTagsByMediaId(mediaId: String): DivaResult<ApiResponse<List<MediaTagResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createMedia(dto: CreateMediaDto): DivaResult<ApiResponse<MediaResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMedia(dto: UpdateMediaDto): DivaResult<ApiResponse<MediaResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMedia(id: String): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMediaTag(dto: DeleteMediaTagDto): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
