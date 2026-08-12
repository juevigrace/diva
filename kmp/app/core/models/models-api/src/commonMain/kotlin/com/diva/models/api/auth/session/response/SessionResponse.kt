package com.diva.models.api.auth.session.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    @SerialName("session_id")
    val sessionId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("type")
    val type: String,
    @SerialName("status")
    val status: String,
    @SerialName("device_id")
    val deviceId: String,
    @SerialName("ip")
    val ip: String = "",
    @SerialName("agent")
    val agent: String,
    @SerialName("access_expires_at")
    val accessExpiresAt: Long,
    @SerialName("refresh_expires_at")
    val refreshExpiresAt: Long,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("updated_at")
    val updatedAt: Long,
)
