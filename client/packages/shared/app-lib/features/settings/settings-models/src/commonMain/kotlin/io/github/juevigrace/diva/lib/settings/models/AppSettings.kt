package io.github.juevigrace.diva.lib.settings.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.Option

@DivaJsExport
interface AppSettings {
    val port: Option<Int>
    val host: String
}
