package com.diva.permissions.api.handler

import com.diva.models.api.ApiResponse
import com.diva.models.api.PaginationResponse
import com.diva.models.api.permissions.dtos.CreatePermissionDto
import com.diva.models.api.permissions.dtos.UpdatePermissionDto
import com.diva.models.api.permissions.response.PermissionResponse
import com.diva.permissions.data.PermissionsService
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface PermissionsHandler {
    suspend fun getPermissions(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<PermissionResponse>>, DivaError>
    suspend fun getPermissionById(id: String): DivaResult<ApiResponse<PermissionResponse>, DivaError>
    suspend fun createPermission(dto: CreatePermissionDto): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun updatePermission(dto: UpdatePermissionDto): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun deletePermission(id: String): DivaResult<ApiResponse<Unit>, DivaError>
}

class PermissionsHandlerImpl(
    private val service: PermissionsService
) : PermissionsHandler {
    override suspend fun getPermissions(
        page: Int,
        pageSize: Int
    ): DivaResult<ApiResponse<PaginationResponse<PermissionResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun getPermissionById(id: String): DivaResult<ApiResponse<PermissionResponse>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createPermission(dto: CreatePermissionDto): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePermission(dto: UpdatePermissionDto): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePermission(id: String): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
