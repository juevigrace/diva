package io.github.juevigrace.diva.lib.models.api.user.state

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class UpdateUserStatusDto(
    @SerialName("status")
    val status: String,
)
