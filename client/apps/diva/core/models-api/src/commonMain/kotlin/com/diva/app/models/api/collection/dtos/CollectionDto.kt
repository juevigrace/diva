package com.diva.app.models.api.collection.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDto(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String = "",
    @SerialName("collection_type")
    val collectionType: String,
    @SerialName("visibility")
    val visibility: String = "PRIVATE",
    @SerialName("cover_media_id")
    val coverMediaId: String? = null,
)
