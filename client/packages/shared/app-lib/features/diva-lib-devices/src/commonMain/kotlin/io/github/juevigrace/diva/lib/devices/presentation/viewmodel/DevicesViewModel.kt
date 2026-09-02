package io.github.juevigrace.diva.lib.devices.presentation.viewmodel

import io.github.juevigrace.diva.lib.devices.domain.DevicesRepository
import io.github.juevigrace.diva.lib.devices.presentation.events.DevicesEvents
import io.github.juevigrace.diva.lib.devices.presentation.state.DevicesState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DevicesViewModel(
    private val repository: DevicesRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<DevicesState>
        field = MutableStateFlow(DevicesState())

    fun onEvent(event: DevicesEvents) {
        when (event) {
            DevicesEvents.OnBack -> navigator.pop()
        }
    }
}
