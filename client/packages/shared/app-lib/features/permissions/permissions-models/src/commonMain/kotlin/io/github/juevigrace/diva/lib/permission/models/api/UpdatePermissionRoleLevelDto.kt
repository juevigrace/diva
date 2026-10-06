@file:DivaJsExport

package io.github.juevigrace.diva.lib.permission.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePermissionRoleLevelDto(
    @SerialName("level")
    val level: String,
)
