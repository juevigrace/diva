@file:DivaJsExport

package io.github.juevigrace.diva.lib.device.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.device.models.api.DeviceResponse

data class Device(
    val id: String,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
) {
    companion object {
        fun fromResponse(response: DeviceResponse): Device {
            return Device(
                id = response.id,
                name = response.name,
                createdAt = response.createdAt,
                updatedAt = response.updatedAt,
            )
        }
    }
}
