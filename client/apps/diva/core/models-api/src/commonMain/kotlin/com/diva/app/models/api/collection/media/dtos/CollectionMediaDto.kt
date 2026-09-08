package com.diva.app.models.api.collection.media.dtos

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
