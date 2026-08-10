package com.diva.social.api.handler

import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.social.post.dtos.CreatePostDto
import com.diva.models.api.social.post.response.PostAttachmentResponse
import com.diva.models.api.social.post.response.PostResponse
import com.diva.social.data.post.PostService
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface PostHandler {
    suspend fun getPosts(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<PostResponse>>, DivaError>
    suspend fun getPostById(id: String): DivaResult<ApiResponse<PostResponse>, DivaError>
    suspend fun getPostAttachments(postId: String): DivaResult<ApiResponse<List<PostAttachmentResponse>>, DivaError>
    suspend fun createPost(dto: CreatePostDto): DivaResult<ApiResponse<PostResponse>, DivaError>
    suspend fun deletePost(id: String): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun deletePostAttachment(id: String): DivaResult<ApiResponse<Unit>, DivaError>
}

class PostHandlerImpl(
    private val service: PostService
) : PostHandler {
    override suspend fun getPosts(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<PostResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostById(id: String): DivaResult<ApiResponse<PostResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostAttachments(postId: String): DivaResult<ApiResponse<List<PostAttachmentResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createPost(dto: CreatePostDto): DivaResult<ApiResponse<PostResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePost(id: String): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePostAttachment(id: String): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
