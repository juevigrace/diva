package io.github.juevigrace.diva.lib.models.media

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
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
