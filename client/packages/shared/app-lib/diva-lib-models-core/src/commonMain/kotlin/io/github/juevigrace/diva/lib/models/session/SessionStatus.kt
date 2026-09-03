package io.github.juevigrace.diva.lib.models.session

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
enum class SessionStatus {
    ACTIVE,
    EXPIRED,
    CLOSED,
}

fun safeSessionStatus(value: String): SessionStatus {
    return try {
        SessionStatus.valueOf(value)
    } catch (_: IllegalArgumentException) {
        SessionStatus.EXPIRED
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
enum class SessionType {
    NORMAL,
    TEMPORAL,
}

fun safeSessionType(value: String): SessionType {
    return try {
        SessionType.valueOf(value)
    } catch (_: IllegalArgumentException) {
        SessionType.NORMAL
    }
}
