package com.diva.models.user.device

import com.diva.models.api.user.device.response.UserDeviceResponse
import com.diva.models.device.Device
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class UserDevice(
    val userId: Uuid,
    val device: Device,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun fromResponse(response: UserDeviceResponse): UserDevice {
            return UserDevice(
                userId = Uuid.parse(response.userId),
                device = Device(
                    id = Uuid.parse(response.deviceId),
                    name = response.deviceName,
                    createdAt = Instant.fromEpochMilliseconds(response.createdAt),
                    updatedAt = Instant.fromEpochMilliseconds(response.updatedAt),
                ),
                createdAt = Instant.fromEpochMilliseconds(response.createdAt),
                updatedAt = Instant.fromEpochMilliseconds(response.updatedAt),
            )
        }
    }
}
