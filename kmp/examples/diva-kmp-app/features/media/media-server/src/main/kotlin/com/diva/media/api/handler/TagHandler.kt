package com.diva.media.api.handler

import com.diva.media.data.TagService
import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.media.dtos.CreateTagDto
import com.diva.models.api.media.dtos.UpdateTagDto
import com.diva.models.api.media.response.TagResponse
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface TagHandler {
    suspend fun getTags(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<TagResponse>>, DivaError>
    suspend fun getTag(id: String): DivaResult<ApiResponse<TagResponse>, DivaError>
    suspend fun createTag(dto: CreateTagDto): DivaResult<ApiResponse<TagResponse>, DivaError>
    suspend fun updateTag(dto: UpdateTagDto): DivaResult<ApiResponse<TagResponse>, DivaError>
    suspend fun deleteTag(id: String): DivaResult<ApiResponse<Unit>, DivaError>
}

class TagHandlerImpl(
    private val service: TagService
) : TagHandler {
    override suspend fun getTags(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<TagResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun getTag(id: String): DivaResult<ApiResponse<TagResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createTag(dto: CreateTagDto): DivaResult<ApiResponse<TagResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTag(dto: UpdateTagDto): DivaResult<ApiResponse<TagResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTag(id: String): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
