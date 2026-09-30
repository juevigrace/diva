@file:OptIn(kotlin.js.ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.settings.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.Option

interface AppSettings {
    val port: Option<Int>
    val host: String
}
