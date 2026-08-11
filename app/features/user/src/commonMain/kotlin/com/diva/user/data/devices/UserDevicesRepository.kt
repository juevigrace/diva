package com.diva.user.data.devices

import com.diva.auth.session.data.SessionRepository
import com.diva.models.Repository
import com.diva.models.api.device.response.DeviceResponse
import com.diva.models.api.user.device.response.UserDeviceResponse
import com.diva.user.api.client.devices.UserDevicesApi
import kotlin.uuid.ExperimentalUuidApi

interface UserDevicesRepository : Repository {
    suspend fun getUserDevices(): Result<List<UserDeviceResponse>>
    suspend fun getUserDevice(did: String): Result<UserDeviceResponse>
    suspend fun deleteUserDevice(did: String): Result<Unit>
    suspend fun listAllDevices(): Result<List<DeviceResponse>>
}

class UserDevicesRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val client: UserDevicesApi,
) : UserDevicesRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getUserDevices(): Result<List<UserDeviceResponse>> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.getUserDevices(session.user.id.toString(), session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getUserDevice(did: String): Result<UserDeviceResponse> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.getUserDevice(session.user.id.toString(), did, session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun deleteUserDevice(did: String): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.deleteUserDevice(session.user.id.toString(), did, session.accessToken)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun listAllDevices(): Result<List<DeviceResponse>> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.listAll(session.accessToken)
        }
    }
}
