package com.diva.app.player.presentation.ui.util

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull

private const val MILLIS_PER_SECOND = 1000L
private const val SECONDS_PER_MINUTE = 60L
private const val TIME_PAD_LENGTH = 2
private const val TIME_PAD_CHAR = '0'

fun formatDuration(ms: Long): String {
    val totalSeconds = ms / MILLIS_PER_SECOND
    val minutes = totalSeconds / SECONDS_PER_MINUTE
    val seconds = totalSeconds % SECONDS_PER_MINUTE
    return "$minutes:${seconds.toString().padStart(TIME_PAD_LENGTH, TIME_PAD_CHAR)}"
}

fun Option<Long>.durationLabel(): String = getOrNull()?.let { formatDuration(it) } ?: "--:--"
