package com.diva.app.features.search.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.ic_search
import com.diva.app.generated.resources.search
import io.github.juevigrace.diva.ui.navigation.Tab
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Serializable
data object Search : NavKey, Tab {
    override val route: NavKey
        get() = this

    override val icon: DrawableResource = Res.drawable.ic_search
    override val title: StringResource = Res.string.search
}

typealias SearchRoute = Search