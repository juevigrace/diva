package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.database.user.devices.UserDevicesStorage
import io.github.juevigrace.diva.lib.models.user.device.UserDevice
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.user.data.api.client.UserDevicesApi
import io.github.juevigrace.diva.lib.user.domain.UserDevicesRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow

class UserDevicesRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserDevicesStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserDevicesApi,
) : UserDevicesRepository {
    override fun getDevices(userId: String): Flow<Result<List<UserDevice>>> = storage.findAllFlow(userId)

    override fun getDevice(userId: String, deviceId: String): Flow<Result<Option<UserDevice>>> =
        storage.findOneFlow(userId, deviceId)

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
