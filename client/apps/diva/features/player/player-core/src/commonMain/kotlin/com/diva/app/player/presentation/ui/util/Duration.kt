package com.diva.app.player.presentation.ui.util

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull

fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    return "%d:%02d".format(totalSeconds / 60, totalSeconds % 60)
}

fun Option<Long>.durationLabel(): String = getOrNull()?.let { formatDuration(it) } ?: "--:--"