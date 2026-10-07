package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.session.domain.withSessionFlow
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
    override fun getDevices(): Flow<Result<List<UserDevice>>> {
        return withSessionFlow(sessionRepository::getCurrent) { session ->
            storage.findAllFlow(session.userId).collect { result -> emit(result) }
        }
    }

    override fun getDevice(deviceId: String): Flow<Result<UserDevice>> {
        return withSessionFlow(sessionRepository::getCurrent) { session ->
            storage.findOneFlow(session.userId, deviceId).collect { result ->
            }
        }
    }


    override suspend fun sync(): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::getCurrent,
            onFound = { session ->
                api.list(
                    uid = userId,
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

    override suspend fun save(device: UserDevice): Result<Unit> = storage.upsert(device)

    override suspend fun delete(userId: String, deviceId: String): Result<Unit> = storage.deleteOne(userId, deviceId)
}
