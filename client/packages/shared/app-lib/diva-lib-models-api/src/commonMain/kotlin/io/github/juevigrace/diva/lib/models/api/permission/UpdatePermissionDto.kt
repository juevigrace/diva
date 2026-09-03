package io.github.juevigrace.diva.lib.models.api.permission

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class UpdatePermissionDto(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
)
