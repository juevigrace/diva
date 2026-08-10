package com.diva.user.api.handler

import com.diva.models.api.ApiResponse
import com.diva.models.api.user.permissions.dtos.DeleteUserPermissionDto
import com.diva.models.api.user.permissions.dtos.UserPermissionDto
import com.diva.models.api.user.permissions.response.UserPermissionResponse
import com.diva.user.data.UserPermissionsService
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface UserPermissionsHandler {
    suspend fun getPermissions(userId: String): DivaResult<ApiResponse<List<UserPermissionResponse>>, DivaError>
    suspend fun createPermission(dto: UserPermissionDto): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun updatePermission(dto: UserPermissionDto): DivaResult<ApiResponse<Unit>, DivaError>
    suspend fun deletePermission(dto: DeleteUserPermissionDto): DivaResult<ApiResponse<Unit>, DivaError>
}

class UserPermissionsHandlerImpl(
    private val service: UserPermissionsService
) : UserPermissionsHandler {
    override suspend fun getPermissions(
        userId: String
    ): DivaResult<ApiResponse<List<UserPermissionResponse>>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun createPermission(dto: UserPermissionDto): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePermission(dto: UserPermissionDto): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePermission(dto: DeleteUserPermissionDto): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
