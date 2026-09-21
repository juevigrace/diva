@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.api.collection.media.response

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionMediaResponse(
    @SerialName("media_id")
    val mediaId: String,
    @SerialName("position")
    val position: Int,
    @SerialName("added_by")
    val addedBy: String,
    @SerialName("score")
    val score: Float = 0f,
    @SerialName("added_at")
    val addedAt: Long,
)
