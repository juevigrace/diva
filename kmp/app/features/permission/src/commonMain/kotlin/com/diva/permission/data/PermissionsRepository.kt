package com.diva.permission.data

import com.diva.auth.session.data.SessionRepository
import com.diva.models.Repository
import com.diva.models.api.pagination.response.PaginationResponse
import com.diva.models.api.permission.dtos.UpdatePermissionDto
import com.diva.models.api.permission.dtos.UpdatePermissionRoleLevelDto
import com.diva.models.api.permission.response.PermissionResponse
import com.diva.permission.api.client.PermissionsApi
import kotlin.uuid.ExperimentalUuidApi

interface PermissionsRepository : Repository {
    suspend fun getPermissions(page: Int, pageSize: Int): Result<PaginationResponse<PermissionResponse>>
    suspend fun getPermission(pid: String): Result<PermissionResponse>
    suspend fun updatePermission(pid: String, dto: UpdatePermissionDto): Result<Unit>
    suspend fun updatePermissionRoleLevel(pid: String, dto: UpdatePermissionRoleLevelDto): Result<PermissionResponse>
}

class PermissionsRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val client: PermissionsApi,
) : PermissionsRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getPermissions(
        page: Int,
        pageSize: Int
    ): Result<PaginationResponse<PermissionResponse>> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.list(page, pageSize, session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getPermission(pid: String): Result<PermissionResponse> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.getByID(pid, session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updatePermission(pid: String, dto: UpdatePermissionDto): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.update(pid, dto, session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updatePermissionRoleLevel(
        pid: String,
        dto: UpdatePermissionRoleLevelDto
    ): Result<PermissionResponse> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.updateRoleLevel(pid, dto, session.accessToken)
        }
    }
}
