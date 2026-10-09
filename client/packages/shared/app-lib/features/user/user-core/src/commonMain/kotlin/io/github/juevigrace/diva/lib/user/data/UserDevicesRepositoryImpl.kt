package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.data.api.client.UserDevicesApi
import io.github.juevigrace.diva.lib.user.database.devices.UserDevicesStorage
import io.github.juevigrace.diva.lib.user.device.models.UserDevice
import io.github.juevigrace.diva.lib.user.domain.UserDevicesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class UserDevicesRepositoryImpl(
    private val storage: UserDevicesStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserDevicesApi,
) : UserDevicesRepository {
    override suspend fun get(id: String): Result<List<UserDevice>> = storage.findAll(id)

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
