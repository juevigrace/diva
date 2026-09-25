@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.device

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.api.device.DeviceResponse
import kotlin.js.ExperimentalJsExport

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
