package com.diva.models.api.permission.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePermissionDto(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
)
