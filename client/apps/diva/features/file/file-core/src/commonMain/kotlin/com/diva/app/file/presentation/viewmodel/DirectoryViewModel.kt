package com.diva.app.file.presentation.viewmodel

import com.diva.app.file.domain.DirectoryRepository
import com.diva.app.file.models.Directory
import com.diva.app.file.presentation.events.DirectoryEvents
import com.diva.app.file.presentation.state.DirectoryState
import com.diva.app.file.presentation.ui.components.navigation.DirectoryRoute
import com.diva.app.file.presentation.ui.components.navigation.Files
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DirectoryViewModel(
    private val tabNavigator: TabNavigator,
    private val repository: DirectoryRepository,
) : DivaViewModel() {

    private val currentDirectoryId = MutableStateFlow<String?>(null)
    private val breadcrumbs = MutableStateFlow<List<Directory>>(emptyList())

    val state: StateFlow<DirectoryState> = combine(
        currentDirectoryId,
        breadcrumbs,
        currentDirectoryId.flatMapLatest { directoryId -> repository.observeChildren(directoryId) },
    ) { id, trail, entriesResult ->
        DirectoryState(
            currentDirectoryId = id,
            breadcrumbs = trail,
            entries = entriesResult.getOrDefault(emptyList()),
        )
    }.stateIn(scope, SharingStarted.WhileSubscribed(5_000), DirectoryState())

    fun onEvent(event: DirectoryEvents) {
        when (event) {
            is DirectoryEvents.OnOpenDirectory -> tabNavigator.navigate(DirectoryRoute(event.directoryId))
            is DirectoryEvents.OnNavigateToBreadcrumb ->
                if (event.directoryId.isEmpty()) {
                    tabNavigator.popUntil(Files)
                } else {
                    tabNavigator.popUntil(DirectoryRoute(event.directoryId))
                }
        }
    }

    /**
     * Called when the files tab becomes visible or a new [DirectoryRoute] is pushed.
     * Switching directories swaps the entries flow and reloads breadcrumbs; the
     * level being opened is always rescanned so the cache stays fresh on revisit.
     */
    fun onEnter(directoryId: String?) {
        if (directoryId != currentDirectoryId.value) {
            currentDirectoryId.value = directoryId
            breadcrumbs.value = emptyList()
            scope.launch { breadcrumbs.value = repository.breadcrumbs(directoryId) }
        }
        scope.launch { repository.refresh(directoryId) }
    }
}
