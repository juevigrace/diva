package io.github.juevigrace.diva.lib.devices.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Devices : NavKey

typealias DevicesRoute = Devices