package com.diva.app.file.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.files
import com.diva.app.generated.resources.ic_folder
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

/**
 * A single directory inside the files tab, pushed on top of the tab so back
 * walks up the tree. Ids are strings to match [com.diva.app.file.models.Directory.id].
 */
@Serializable
data class DirectoryDetail(
    val directoryId: String,
) : NavKey

typealias DirectoryRoute = DirectoryDetail