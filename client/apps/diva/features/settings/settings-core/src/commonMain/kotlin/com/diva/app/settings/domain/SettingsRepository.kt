package com.diva.app.settings.domain

import com.diva.app.settings.models.AppSettings
import io.github.juevigrace.diva.lib.settings.domain.SharedSettingsRepository

interface SettingsRepository : SharedSettingsRepository<AppSettings>
