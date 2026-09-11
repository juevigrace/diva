@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.player

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport

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
