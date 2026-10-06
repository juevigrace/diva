@file:DivaJsExport

package io.github.juevigrace.diva.lib.user.preferences.models

import io.github.juevigrace.diva.core.DivaJsExport

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
