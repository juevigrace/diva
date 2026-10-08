@file:DivaJsExport

package com.diva.app.settings.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.settings.models.Settings

data class AppSettings(
    override val protocol: String = "http",
    override val port: Option<Int> = Option.of(8080),
    override val host: String = "localhost",
    override val isDesktop: Boolean = false,
) : Settings
