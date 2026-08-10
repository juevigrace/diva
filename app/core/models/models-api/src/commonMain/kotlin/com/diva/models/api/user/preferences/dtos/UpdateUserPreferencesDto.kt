package com.diva.models.api.user.preferences.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserPreferencesDto(
    @SerialName("theme")
    val theme: String,
    @SerialName("language")
    val language: String,
)
