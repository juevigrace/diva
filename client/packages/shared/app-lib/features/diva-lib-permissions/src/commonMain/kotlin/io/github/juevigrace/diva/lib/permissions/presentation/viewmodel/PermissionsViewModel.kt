package io.github.juevigrace.diva.lib.permissions.presentation.viewmodel

import io.github.juevigrace.diva.lib.permissions.domain.PermissionsRepository
import io.github.juevigrace.diva.lib.permissions.presentation.events.PermissionsEvents
import io.github.juevigrace.diva.lib.permissions.presentation.state.PermissionsState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionsViewModel(
    private val repository: PermissionsRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<PermissionsState>
        field = MutableStateFlow(PermissionsState())

    fun onEvent(event: PermissionsEvents) {
        when (event) {
            PermissionsEvents.OnBack -> navigator.pop()
        }
    }
}
