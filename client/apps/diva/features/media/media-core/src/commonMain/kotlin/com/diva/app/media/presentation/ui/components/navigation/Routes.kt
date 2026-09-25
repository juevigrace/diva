package com.diva.app.media.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Media : NavKey

typealias MediaRoute = Media