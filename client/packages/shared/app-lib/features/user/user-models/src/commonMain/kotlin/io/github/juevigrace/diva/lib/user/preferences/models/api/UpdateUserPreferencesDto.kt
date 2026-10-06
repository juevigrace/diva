@file:DivaJsExport

package io.github.juevigrace.diva.lib.user.preferences.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserPreferencesDto(
    @SerialName("theme")
    val theme: String,
    @SerialName("language")
    val language: String,
)
