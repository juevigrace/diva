package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.data.api.client.UserPermissionsApi
import io.github.juevigrace.diva.lib.user.database.permissions.UserPermissionsStorage
import io.github.juevigrace.diva.lib.user.domain.UserPermissionsRepository
import io.github.juevigrace.diva.lib.user.permissions.models.UserPermission
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPermissionsRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserPermissionsStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserPermissionsApi,
) : UserPermissionsRepository {
    override fun observe(): Flow<Result<List<UserPermission>>> = observeSession(sessionRepository::observe) { session ->
        storage.findAllFlow(session.userId)
    }

    override fun observe(permissionId: String): Flow<Result<UserPermission>> =
        observeSession(sessionRepository::observe) { session ->
            storage.findOneFlow(permissionId, session.userId).map { result ->
                result.mapCatching { option ->
                    option.getOrThrow { IllegalStateException("No permission '$permissionId' for user '${session.userId}'") }
                }
            }
        }

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
