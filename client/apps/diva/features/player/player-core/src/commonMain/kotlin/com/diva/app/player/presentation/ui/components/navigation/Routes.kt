package com.diva.app.player.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import com.diva.app.models.media.MediaType
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val mediaType: MediaType,
    val trackId: Long? = null,
    val uri: String? = null,
) : NavKey

typealias PlayerRoute = Player