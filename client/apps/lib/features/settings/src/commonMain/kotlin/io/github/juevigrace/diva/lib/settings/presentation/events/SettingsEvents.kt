package io.github.juevigrace.diva.lib.settings.presentation.events

sealed interface SettingsEvents {
    data object OnBack : SettingsEvents
}
