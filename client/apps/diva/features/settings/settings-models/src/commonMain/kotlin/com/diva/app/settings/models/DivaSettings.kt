@file:DivaJsExport

package com.diva.app.settings.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.settings.models.AppSettings

data class DivaSettings(
    override val port: Option<Int>,
    override val host: String,
    override val isDesktop: Boolean,
) : AppSettings
