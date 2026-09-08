package com.diva.app.models.api.collection.playlist.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistSuggestionsDto(
    @SerialName("suggester_id")
    val suggesterId: String? = null,
    @SerialName("media_id")
    val mediaId: String,
)
