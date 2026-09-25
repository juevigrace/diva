package com.diva.app.collection.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val albumId: Long,
) : NavKey

typealias AlbumRoute = Album