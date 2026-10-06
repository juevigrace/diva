@file:DivaJsExport

package io.github.juevigrace.diva.lib.permission.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePermissionDto(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
)
