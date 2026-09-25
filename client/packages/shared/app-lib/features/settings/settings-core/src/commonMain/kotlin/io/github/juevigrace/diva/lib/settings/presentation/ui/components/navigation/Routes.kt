package io.github.juevigrace.diva.lib.settings.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Settings : NavKey

typealias SettingsRoute = Settings