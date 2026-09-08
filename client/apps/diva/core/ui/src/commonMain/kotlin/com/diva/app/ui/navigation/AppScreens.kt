package com.diva.app.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.diva.app.models.media.MediaType
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreens : NavKey {

    @Serializable
    sealed interface HomeGraph : AppScreens {
        @Serializable
        data object Home : HomeGraph

        @Serializable
        data object Library : HomeGraph

        @Serializable
        data object Playlists : HomeGraph

        @Serializable
        data object Search : HomeGraph

        @Serializable
        data object ServerSetup : HomeGraph
    }

    @Serializable
    sealed interface PlayerGraph : AppScreens {
        @Serializable
        data class Player(
            val mediaType: MediaType,
            val trackId: Long? = null,
            val uri: String? = null,
        ) : PlayerGraph
    }

    @Serializable
    data class Folder(val folderId: Long) : AppScreens

    @Serializable
    data class Album(val albumId: Long) : AppScreens
}

typealias HomeRoute = AppScreens.HomeGraph.Home
typealias LibraryRoute = AppScreens.HomeGraph.Library
typealias PlaylistsRoute = AppScreens.HomeGraph.Playlists
typealias SearchRoute = AppScreens.HomeGraph.Search
typealias ServerSetupRoute = AppScreens.HomeGraph.ServerSetup
typealias PlayerRoute = AppScreens.PlayerGraph.Player
typealias FolderRoute = AppScreens.Folder
typealias AlbumRoute = AppScreens.Album