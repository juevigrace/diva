package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.user.database.permissions.UserPermissionsStorage
import io.github.juevigrace.diva.lib.models.user.permissions.UserPermission
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.user.data.api.client.UserPermissionsApi
import io.github.juevigrace.diva.lib.user.domain.UserPermissionsRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow

class UserPermissionsRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserPermissionsStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserPermissionsApi,
) : UserPermissionsRepository {
    override fun getPermissions(userId: String): Flow<Result<List<UserPermission>>> = storage.findAllFlow(userId)

    override fun getPermission(permissionId: String, userId: String): Flow<Result<Option<UserPermission>>> =
        storage.findOneFlow(permissionId, userId)

    override suspend fun sync(userId: String): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::getCurrent,
            onFound = { session ->
                api.list(
                    uid = userId,
                    token = session.accessToken
                ).mapCatching { responses ->
                    val results = responses.map {
                        scope.async {
                            storage.upsert(userId, UserPermission.fromResponse(it))
                        }
                    }.awaitAll()

                    val failures = results.mapNotNull { it.exceptionOrNull() }
                    if (failures.isNotEmpty()) {
                        val error = IllegalStateException(
                            "Failed to upsert ${failures.size} of ${results.size} user permissions"
                        )
                        failures.forEach(error::addSuppressed)
                        throw error
                    }
                }
            },
        )
    }

    override suspend fun save(
        userId: String,
        permission: UserPermission
    ): Result<Unit> = storage.upsert(userId, permission)

    override suspend fun delete(permissionId: String, userId: String): Result<Unit> = storage.deleteOne(permissionId, userId)
}
