package io.github.juevigrace.diva.lib.models.api.user.preferences

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class UpdateUserPreferencesDto(
    @SerialName("theme")
    val theme: String,
    @SerialName("language")
    val language: String,
)
