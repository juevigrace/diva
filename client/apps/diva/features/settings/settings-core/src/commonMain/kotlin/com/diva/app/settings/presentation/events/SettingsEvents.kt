package com.diva.app.settings.presentation.events

sealed interface SettingsEvents {
    data object OnBack : SettingsEvents
}
