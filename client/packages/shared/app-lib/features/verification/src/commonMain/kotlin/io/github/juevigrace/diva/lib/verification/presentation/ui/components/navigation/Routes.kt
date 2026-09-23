package io.github.juevigrace.diva.lib.verification.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Verification : NavKey

typealias VerificationRoute = Verification