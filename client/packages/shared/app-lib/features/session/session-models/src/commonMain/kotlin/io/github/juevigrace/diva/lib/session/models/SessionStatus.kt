@file:DivaJsExport

package io.github.juevigrace.diva.lib.session.models

import io.github.juevigrace.diva.core.DivaJsExport

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

enum class SessionType {
    NORMAL,
    TEMPORAL,
    LOCAL,
}

fun safeSessionType(value: String): SessionType {
    return try {
        SessionType.valueOf(value)
    } catch (_: IllegalArgumentException) {
        SessionType.NORMAL
    }
}
