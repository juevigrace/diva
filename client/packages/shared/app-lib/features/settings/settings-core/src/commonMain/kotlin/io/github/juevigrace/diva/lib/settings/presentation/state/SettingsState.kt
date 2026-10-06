package io.github.juevigrace.diva.lib.settings.presentation.state

import io.github.juevigrace.diva.lib.settings.models.AppSettings

data class SettingsState(
    val isLoading: Boolean = false,
    val settings: Result<AppSettings> = Result.failure(IllegalStateException("Settings not loaded")),
)
