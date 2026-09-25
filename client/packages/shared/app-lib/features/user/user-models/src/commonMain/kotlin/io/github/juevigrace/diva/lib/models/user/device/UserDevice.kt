@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.user.device

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.user.api.device.UserDeviceResponse
import io.github.juevigrace.diva.lib.models.device.Device
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
