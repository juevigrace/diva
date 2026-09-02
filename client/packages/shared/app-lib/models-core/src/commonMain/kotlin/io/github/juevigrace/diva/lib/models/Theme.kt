package io.github.juevigrace.diva.lib.models

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
