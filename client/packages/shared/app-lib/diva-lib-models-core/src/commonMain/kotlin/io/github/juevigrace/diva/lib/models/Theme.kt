@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport

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
