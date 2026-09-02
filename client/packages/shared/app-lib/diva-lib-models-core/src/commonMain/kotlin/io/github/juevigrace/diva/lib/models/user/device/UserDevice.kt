package io.github.juevigrace.diva.lib.models.user.device

import io.github.juevigrace.diva.lib.models.api.user.device.UserDeviceResponse
import io.github.juevigrace.diva.lib.models.device.Device
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
