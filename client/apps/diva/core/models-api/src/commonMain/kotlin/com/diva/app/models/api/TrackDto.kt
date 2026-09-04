package com.diva.app.models.api

import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
    val id: String,
    val title: String,
    val artist: String,
    val streamUrl: String,
)
