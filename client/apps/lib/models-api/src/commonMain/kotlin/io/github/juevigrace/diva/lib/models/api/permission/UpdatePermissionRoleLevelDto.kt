package io.github.juevigrace.diva.lib.models.api.permission

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePermissionRoleLevelDto(
    @SerialName("level")
    val level: String,
)
