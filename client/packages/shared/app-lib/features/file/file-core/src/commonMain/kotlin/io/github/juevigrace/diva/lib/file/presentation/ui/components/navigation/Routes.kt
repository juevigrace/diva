package io.github.juevigrace.diva.lib.file.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.generated.resources.Res
import io.github.juevigrace.diva.lib.generated.resources.files
import io.github.juevigrace.diva.lib.generated.resources.ic_folder
import io.github.juevigrace.diva.ui.navigation.Tab
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Serializable
data object Files : NavKey, Tab {
    override val route: NavKey
        get() = this

    override val icon: DrawableResource = Res.drawable.ic_folder
    override val title: StringResource = Res.string.files
}

typealias FilesRoute = Files
