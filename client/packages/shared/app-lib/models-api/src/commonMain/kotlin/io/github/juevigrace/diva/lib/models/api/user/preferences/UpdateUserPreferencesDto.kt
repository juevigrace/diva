package io.github.juevigrace.diva.lib.models.api.user.preferences

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserPreferencesDto(
    @SerialName("theme")
    val theme: String,
    @SerialName("language")
    val language: String,
)
