@file:DivaJsExport

package com.diva.app.player.models

import io.github.juevigrace.diva.core.DivaJsExport

enum class RepeatMode {
    NONE,
    ONE,
    ALL,
    UNSPECIFIED,
}

fun safeRepeatMode(value: String): RepeatMode {
    return try {
        RepeatMode.valueOf(value)
    } catch (_: IllegalArgumentException) {
        RepeatMode.UNSPECIFIED
    }
}
