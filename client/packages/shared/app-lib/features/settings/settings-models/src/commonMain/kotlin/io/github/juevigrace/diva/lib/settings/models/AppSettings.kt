@file:DivaJsExport

package io.github.juevigrace.diva.lib.settings.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.Option

interface AppSettings {
    val protocol: String
    val port: Option<Int>
    val host: String
    val isDesktop: Boolean
}
