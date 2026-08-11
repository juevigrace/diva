package com.diva.user.data.permissions

import com.diva.auth.session.data.SessionRepository
import com.diva.models.Repository
import com.diva.models.api.user.permissions.dtos.CreateUserPermissionDto
import com.diva.models.api.user.permissions.dtos.UpdateUserPermissionDto
import com.diva.models.api.user.permissions.response.UserPermissionResponse
import com.diva.user.api.client.permissions.UserPermissionsApi
import kotlin.uuid.ExperimentalUuidApi

interface UserPermissionsRepository : Repository {
    suspend fun getUserPermissions(): Result<List<UserPermissionResponse>>
    suspend fun getUserPermission(pid: String): Result<UserPermissionResponse>
    suspend fun createPermission(dto: CreateUserPermissionDto): Result<Unit>
    suspend fun updatePermission(pid: String, dto: UpdateUserPermissionDto): Result<Unit>
    suspend fun deletePermission(pid: String): Result<Unit>
}

class UserPermissionsRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val client: UserPermissionsApi,
) : UserPermissionsRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getUserPermissions(): Result<List<UserPermissionResponse>> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.getByUser(session.user.id.toString(), session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getUserPermission(pid: String): Result<UserPermissionResponse> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.getOneByUser(session.user.id.toString(), pid, session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createPermission(dto: CreateUserPermissionDto): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.create(session.user.id.toString(), dto, session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updatePermission(pid: String, dto: UpdateUserPermissionDto): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.update(session.user.id.toString(), pid, dto, session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deletePermission(pid: String): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.delete(session.user.id.toString(), pid, session.accessToken)
        }
    }
}
