@file:DivaJsExport

package com.diva.app.collection.playlist.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistResponse(
    @SerialName("collection_id")
    val collectionId: String,
    @SerialName("is_collaborative")
    val isCollaborative: Boolean = false,
    @SerialName("allow_suggestions")
    val allowSuggestions: Boolean = true,
    // TODO: two endpoints for these fields?
    @SerialName("contributors")
    val contributors: List<String> = emptyList(),
    @SerialName("suggestions")
    val suggestions: List<PlaylistSuggestionsResponse> = emptyList(),
)
