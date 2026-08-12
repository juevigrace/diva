package com.diva.models.api.user.state.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserStatusDto(
    @SerialName("status")
    val status: String,
)
