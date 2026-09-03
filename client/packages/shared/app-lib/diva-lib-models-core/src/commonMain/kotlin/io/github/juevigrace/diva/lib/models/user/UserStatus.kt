package io.github.juevigrace.diva.lib.models.user

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
enum class UserStatus {
    ACTIVE,
    SUSPENDED,
    INACTIVE,
}

fun safeUserStatus(value: String): UserStatus {
    return try {
        UserStatus.valueOf(value)
    } catch (_: IllegalArgumentException) {
        UserStatus.ACTIVE
    }
}
