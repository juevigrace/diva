package com.diva.app.library.presentation.viewmodel

import com.diva.app.folder.presentation.ui.components.navigation.FolderRoute
import com.diva.app.library.presentation.events.LibraryEvents
import com.diva.app.library.presentation.state.LibraryFilter
import com.diva.app.library.presentation.state.LibraryState
import com.diva.app.library.presentation.state.defaultFavoriteIds
import com.diva.app.library.presentation.state.libraryFolders
import com.diva.app.library.presentation.state.librarySections
import com.diva.app.player.presentation.ui.components.navigation.PlayerRoute
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LibraryViewModel(
    private val navigator: Navigator,
    private val tabNavigator: TabNavigator,
) : DivaViewModel() {

    // TODO(ui-pass): restore repository wiring
    private val favoriteIds = defaultFavoriteIds().toMutableSet()

    val state: StateFlow<LibraryState>
        field = MutableStateFlow(
            LibraryState(
                sections = librarySections(LibraryFilter.ALL, favoriteIds),
                folders = libraryFolders(),
                favoriteIds = favoriteIds.toSet(),
            )
        )

    fun onEvent(event: LibraryEvents) {
        when (event) {
            is LibraryEvents.OnFilterChange -> setFilter(event.filter)
            is LibraryEvents.OnToggleFavorite -> toggleFavorite(event)
            is LibraryEvents.OnOpenMedia -> openMedia(event)
            is LibraryEvents.OnOpenFolder -> tabNavigator.navigate(FolderRoute(event.folderId))
        }
    }

    private fun setFilter(filter: LibraryFilter) {
        state.update { current ->
            current.copy(
                filter = filter,
                sections = librarySections(filter, favoriteIds),
            )
        }
    }

    private fun toggleFavorite(event: LibraryEvents.OnToggleFavorite) {
        if (!favoriteIds.remove(event.media.id)) {
            favoriteIds.add(event.media.id)
        }
        val filter = state.value.filter
        state.update { current ->
            current.copy(
                sections = librarySections(filter, favoriteIds),
                favoriteIds = favoriteIds.toSet(),
            )
        }
    }

    private fun openMedia(event: LibraryEvents.OnOpenMedia) {
        navigator.navigate(PlayerRoute(mediaType = event.media.mediaType))
    }
}