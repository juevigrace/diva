package com.diva.app.home.presentation.state

import com.diva.app.home.presentation.ui.models.HomeSection
import com.diva.app.settings.models.AppSettings

data class HomeState(
    val settings: AppSettings = AppSettings(),
    val sections: List<HomeSection> = emptyList(),
)
