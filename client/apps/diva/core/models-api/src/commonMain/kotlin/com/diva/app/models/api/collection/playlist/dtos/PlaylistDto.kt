package com.diva.app.models.api.collection.playlist.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDto(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String = "",
    @SerialName("is_collaborative")
    val isCollaborative: Boolean = false,
    @SerialName("allow_suggestions")
    val allowSuggestions: Boolean = true,
    @SerialName("cover_media_id")
    val coverMediaId: String? = null,
)
