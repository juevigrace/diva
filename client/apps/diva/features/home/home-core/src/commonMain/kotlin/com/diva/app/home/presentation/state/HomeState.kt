package com.diva.app.home.presentation.state

import com.diva.app.settings.models.AppSettings

data class HomeState(
    val settings: AppSettings = AppSettings(),
)
