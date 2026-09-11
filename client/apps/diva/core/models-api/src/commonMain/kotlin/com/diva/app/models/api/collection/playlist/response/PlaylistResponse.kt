@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.api.collection.playlist.response

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
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
