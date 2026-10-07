package com.diva.app.settings.presentation.state

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.settings.models.AppSettings
import io.github.juevigrace.diva.lib.settings.presentation.state.SharedSettingsState

data class SettingsState(
    override val settings: Option<AppSettings> = Option.none(),
    override val isLoading: Boolean = false,
) : SharedSettingsState
