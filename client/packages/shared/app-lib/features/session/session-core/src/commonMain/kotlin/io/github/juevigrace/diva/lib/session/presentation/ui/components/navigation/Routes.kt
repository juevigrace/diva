package io.github.juevigrace.diva.lib.session.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Session : NavKey

typealias SessionRoute = Session