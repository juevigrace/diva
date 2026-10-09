package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.data.api.client.UserPermissionsApi
import io.github.juevigrace.diva.lib.user.database.permissions.UserPermissionsStorage
import io.github.juevigrace.diva.lib.user.domain.UserPermissionsRepository
import io.github.juevigrace.diva.lib.user.permissions.models.UserPermission
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class UserPermissionsRepositoryImpl(
    private val storage: UserPermissionsStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserPermissionsApi,
) : UserPermissionsRepository {
    override suspend fun get(id: String): Result<List<UserPermission>> = storage.findAll(id)

    override suspend fun sync(): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::get,
            onFound = { session ->
                api.list(
                    uid = session.userId,
                    token = session.accessToken
                ).mapCatching { responses ->
                    val results = responses.map {
                        scope.async {
                            storage.upsert(session.userId, UserPermission.fromResponse(it))
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

    override suspend fun upsert(permission: UserPermission): Result<Unit> = withSession(sessionRepository::get) { session ->
        storage.upsert(session.userId, permission)
    }

    override suspend fun delete(permissionId: String): Result<Unit> = withSession(sessionRepository::get) { session ->
        storage.deleteOne(permissionId, session.userId)
    }
}
