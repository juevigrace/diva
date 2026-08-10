package com.diva.models.api.permission.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePermissionRoleLevelDto(
    @SerialName("level")
    val level: String,
)
