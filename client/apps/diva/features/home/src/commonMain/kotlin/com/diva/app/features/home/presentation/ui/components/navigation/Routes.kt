package com.diva.app.features.home.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.home
import com.diva.app.generated.resources.ic_home
import io.github.juevigrace.diva.ui.navigation.Tab
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Serializable
data object Home : NavKey, Tab {
    override val route: NavKey
        get() = this

    override val icon: DrawableResource = Res.drawable.ic_home
    override val title: StringResource = Res.string.home
}

typealias HomeRoute = Home