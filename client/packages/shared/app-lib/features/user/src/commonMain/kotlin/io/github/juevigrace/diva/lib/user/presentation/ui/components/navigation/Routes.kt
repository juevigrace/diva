package io.github.juevigrace.diva.lib.user.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Account : NavKey

typealias AccountRoute = Account