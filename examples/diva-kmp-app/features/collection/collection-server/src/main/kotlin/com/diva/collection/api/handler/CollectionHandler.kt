package com.diva.collection.api.handler

import com.diva.collection.data.CollectionService
import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.collection.dtos.CreateCollectionDto
import com.diva.models.api.collection.dtos.DeleteCollectionMedia
import com.diva.models.api.collection.dtos.UpdateCollectionDto
import com.diva.models.api.collection.response.CollectionMediaResponse
import com.diva.models.api.collection.response.CollectionResponse
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface CollectionHandler {
    suspend fun getCollections(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<CollectionResponse>>, DivaError>
    suspend fun getCollection(id: String): DivaResult<ApiResponse<CollectionResponse>, DivaError>
    suspend fun getCollectionMedia(
        collectionId: String,
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<CollectionMediaResponse>>, DivaError>
    suspend fun createCollection(dto: CreateCollectionDto): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun updateCollection(dto: UpdateCollectionDto): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun deleteCollection(id: String): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun deleteCollectionMedia(dto: DeleteCollectionMedia): DivaResult<ApiResponse<Unit>, DivaError>
}

class CollectionHandlerImpl(
    private val service: CollectionService
) : CollectionHandler {
    override suspend fun getCollections(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<CollectionResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun getCollection(id: String): DivaResult<ApiResponse<CollectionResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun getCollectionMedia(
        collectionId: String,
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<CollectionMediaResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createCollection(dto: CreateCollectionDto): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCollection(dto: UpdateCollectionDto): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCollection(id: String): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCollectionMedia(dto: DeleteCollectionMedia): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
