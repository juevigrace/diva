package io.github.juevigrace.diva.lib.settings.presentation.state

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.settings.models.Settings

interface SharedSettingsState {
    val settings: Option<Settings>
    val isLoading: Boolean
}
