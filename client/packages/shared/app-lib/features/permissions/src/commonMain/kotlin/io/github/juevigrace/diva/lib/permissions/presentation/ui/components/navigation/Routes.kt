package io.github.juevigrace.diva.lib.permissions.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Permissions : NavKey

typealias PermissionsRoute = Permissions