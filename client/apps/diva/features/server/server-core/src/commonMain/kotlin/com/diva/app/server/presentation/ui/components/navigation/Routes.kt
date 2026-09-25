package com.diva.app.server.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Server : NavKey

typealias ServerRoute = Server