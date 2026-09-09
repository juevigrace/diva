package com.diva.app.models.player

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
