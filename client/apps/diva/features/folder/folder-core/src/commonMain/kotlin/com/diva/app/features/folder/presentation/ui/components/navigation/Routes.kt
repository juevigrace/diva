package com.diva.app.features.folder.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class Folder(
    val folderId: Long,
) : NavKey

typealias FolderRoute = Folder