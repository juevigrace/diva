@file:DivaJsExport

package com.diva.app.media.models

import io.github.juevigrace.diva.core.DivaJsExport

enum class MediaType {
    AUDIO,
    IMAGE,
    VIDEO,
    UNSPECIFIED,
}

fun safeMediaType(value: String): MediaType {
    return try {
        MediaType.valueOf(value)
    } catch (_: IllegalArgumentException) {
        MediaType.UNSPECIFIED
    }
}
