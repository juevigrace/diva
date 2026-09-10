package com.diva.app.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.home
import com.diva.app.generated.resources.ic_home
import com.diva.app.generated.resources.ic_library
import com.diva.app.generated.resources.ic_profile
import com.diva.app.generated.resources.ic_search
import com.diva.app.generated.resources.library
import com.diva.app.generated.resources.profile
import com.diva.app.generated.resources.search
import com.diva.app.models.media.MediaType
import io.github.juevigrace.diva.ui.navigation.Tab
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Serializable
sealed interface AppScreens : NavKey {

    @Serializable
    sealed interface HomeGraph : AppScreens, Tab {
        override val route: NavKey
            get() = this

        @Serializable
        data object Home : HomeGraph {
            override val icon: DrawableResource = Res.drawable.ic_home
            override val title: StringResource = Res.string.home
        }

        @Serializable
        data object Search : HomeGraph {
            override val icon: DrawableResource = Res.drawable.ic_search
            override val title: StringResource = Res.string.search
        }

        @Serializable
        data object Library : HomeGraph {
            override val icon: DrawableResource = Res.drawable.ic_library
            override val title: StringResource = Res.string.library
        }

        @Serializable
        data object Profile : HomeGraph {
            override val icon: DrawableResource = Res.drawable.ic_profile
            override val title: StringResource = Res.string.profile
        }
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
typealias SearchRoute = AppScreens.HomeGraph.Search
typealias LibraryRoute = AppScreens.HomeGraph.Library
typealias ProfileRoute = AppScreens.HomeGraph.Profile
typealias PlayerRoute = AppScreens.PlayerGraph.Player
typealias FolderRoute = AppScreens.Folder
typealias AlbumRoute = AppScreens.Album