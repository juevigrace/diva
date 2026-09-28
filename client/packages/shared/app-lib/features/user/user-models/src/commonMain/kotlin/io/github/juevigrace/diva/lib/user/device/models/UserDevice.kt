@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.user.device.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.user.device.models.api.UserDeviceResponse
import io.github.juevigrace.diva.lib.device.models.Device
import kotlin.js.ExperimentalJsExport

data class UserDevice(
    val userId: String,
    val device: Device,
    val createdAt: Long,
    val updatedAt: Long,
) {
    companion object {
        fun fromResponse(response: UserDeviceResponse): UserDevice {
            return UserDevice(
                userId = response.userId,
                device = Device(
                    id = response.deviceId,
                    name = response.deviceName,
                    createdAt = response.createdAt,
                    updatedAt = response.updatedAt,
                ),
                createdAt = response.createdAt,
                updatedAt = response.updatedAt,
            )
        }
    }
}
