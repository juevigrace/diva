package com.diva.app.features.playlist.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Playlist : NavKey

typealias PlaylistRoute = Playlist