package com.diva.app.folder.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.folders
import com.diva.app.generated.resources.ic_folder
import io.github.juevigrace.diva.ui.navigation.Tab
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Serializable
data object Folders : NavKey, Tab {
    override val route: NavKey
        get() = this

    override val icon: DrawableResource = Res.drawable.ic_folder
    override val title: StringResource = Res.string.folders
}

typealias FoldersRoute = Folders

/**
 * A single directory inside the folders tab, pushed on top of the tab so back
 * walks up the tree. Ids are strings to match [com.diva.app.folder.models.Folder.id].
 */
@Serializable
data class FolderDetail(
    val folderId: String,
) : NavKey

typealias FolderRoute = FolderDetail