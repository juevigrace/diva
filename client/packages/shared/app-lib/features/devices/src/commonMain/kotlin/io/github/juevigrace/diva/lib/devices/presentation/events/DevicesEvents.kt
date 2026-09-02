package io.github.juevigrace.diva.lib.devices.presentation.events

sealed interface DevicesEvents {
    data object OnBack : DevicesEvents
}
