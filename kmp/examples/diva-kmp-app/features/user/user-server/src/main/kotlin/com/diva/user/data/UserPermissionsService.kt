package com.diva.user.data

import com.diva.database.user.UserStorage
import com.diva.models.api.ApiResponse
import com.diva.models.api.user.permissions.dtos.UserPermissionDto
import com.diva.models.api.user.permissions.response.UserPermissionResponse
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface UserPermissionsService {
    suspend fun getPermissions(
        userId: String
    ): DivaResult<ApiResponse<List<UserPermissionResponse>>, DivaError>

    suspend fun createPermission(dto: UserPermissionDto): DivaResult<ApiResponse<Unit>, DivaError>

    suspend fun updatePermission(dto: UserPermissionDto): DivaResult<ApiResponse<Unit>, DivaError>

    suspend fun deletePermission(userId: String, id: String): DivaResult<ApiResponse<Unit>, DivaError>
}

class UserPermissionsServiceImpl(
    private val storage: UserStorage,
) : UserPermissionsService {
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

    override suspend fun deletePermission(
        userId: String,
        id: String
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        TODO("Not yet implemented")
    }
}
