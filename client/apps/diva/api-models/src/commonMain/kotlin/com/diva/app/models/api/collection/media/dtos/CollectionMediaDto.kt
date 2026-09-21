@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.api.collection.media.dtos

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionMediaDto(
    @SerialName("media_id")
    val mediaId: String,
    @SerialName("position")
    val position: Int = 0,
    @SerialName("score")
    val score: Float = 0f,
)
