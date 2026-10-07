package io.github.juevigrace.diva.lib.settings.presentation.state

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.settings.models.AppSettings

interface SharedSettingsState {
    val settings: Option<AppSettings>
    val isLoading: Boolean
}
