package io.github.juevigrace.diva.lib.models

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
enum class Theme {
    LIGHT,
    DARK,
    SYSTEM,
}

fun safeValueOfTheme(value: String): Theme {
    return try {
        Theme.valueOf(value)
    } catch (_: IllegalArgumentException) {
        Theme.SYSTEM
    }
}
