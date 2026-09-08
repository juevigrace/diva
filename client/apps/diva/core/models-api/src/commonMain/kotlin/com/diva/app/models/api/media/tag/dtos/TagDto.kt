package com.diva.app.models.api.media.tag.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    @SerialName("name")
    val name: String,
)
