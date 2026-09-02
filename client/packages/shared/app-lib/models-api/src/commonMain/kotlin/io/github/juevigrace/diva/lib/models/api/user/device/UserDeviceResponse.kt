package io.github.juevigrace.diva.lib.models.api.user.device

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDeviceResponse(
    @SerialName("user_id")
    val userId: String,
    @SerialName("device_id")
    val deviceId: String,
    @SerialName("device_name")
    val deviceName: String,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("updated_at")
    val updatedAt: Long,
)
