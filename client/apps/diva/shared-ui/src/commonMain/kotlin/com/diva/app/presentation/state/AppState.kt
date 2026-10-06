package com.diva.app.presentation.state

import io.github.juevigrace.diva.lib.settings.models.AppSettings

data class AppState(
    val isReady: Boolean = false,
    val settings: AppSettings,
)
