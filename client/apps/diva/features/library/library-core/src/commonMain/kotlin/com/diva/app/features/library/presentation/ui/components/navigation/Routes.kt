package com.diva.app.features.library.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.ic_books
import com.diva.app.generated.resources.library
import io.github.juevigrace.diva.ui.navigation.Tab
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Serializable
data object Library : NavKey, Tab {
    override val route: NavKey
        get() = this

    override val icon: DrawableResource = Res.drawable.ic_books
    override val title: StringResource = Res.string.library
}

typealias LibraryRoute = Library