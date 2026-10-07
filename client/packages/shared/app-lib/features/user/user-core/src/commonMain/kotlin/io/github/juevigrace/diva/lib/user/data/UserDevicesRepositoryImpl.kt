package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.data.api.client.UserDevicesApi
import io.github.juevigrace.diva.lib.user.database.devices.UserDevicesStorage
import io.github.juevigrace.diva.lib.user.device.models.UserDevice
import io.github.juevigrace.diva.lib.user.domain.UserDevicesRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDevicesRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserDevicesStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserDevicesApi,
) : UserDevicesRepository {
    override fun observe(): Flow<Result<List<UserDevice>>> = observeSession(sessionRepository::observe) { session ->
        storage.findAllFlow(session.userId)
    }

    override fun observe(deviceId: String): Flow<Result<UserDevice>> =
        observeSession(sessionRepository::observe) { session ->
            storage.findOneFlow(session.userId, deviceId).map { result ->
                result.mapCatching { option ->
                    option.getOrThrow { IllegalStateException("No device '$deviceId' for user '${session.userId}'") }
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
                            storage.upsert(UserDevice.fromResponse(it))
                        }
                    }.awaitAll()

                    val failures = results.mapNotNull { it.exceptionOrNull() }
                    if (failures.isNotEmpty()) {
                        val error = IllegalStateException(
                            "Failed to upsert ${failures.size} of ${results.size} user devices"
                        )
                        failures.forEach(error::addSuppressed)
                        throw error
                    }
                }
            },
        )
    }

    override suspend fun upsert(device: UserDevice): Result<Unit> = storage.upsert(device)

    override suspend fun delete(deviceId: String): Result<Unit> = withSession(sessionRepository::get) { session ->
        storage.deleteOne(session.userId, deviceId)
    }
}
