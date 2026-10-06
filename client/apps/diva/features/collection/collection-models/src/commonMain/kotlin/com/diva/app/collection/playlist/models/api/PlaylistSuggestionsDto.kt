@file:DivaJsExport

package com.diva.app.collection.playlist.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistSuggestionsDto(
    @SerialName("suggester_id")
    val suggesterId: String? = null,
    @SerialName("media_id")
    val mediaId: String,
)
