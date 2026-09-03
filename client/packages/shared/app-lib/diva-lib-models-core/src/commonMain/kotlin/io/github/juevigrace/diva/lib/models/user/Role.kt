package io.github.juevigrace.diva.lib.models.user

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
enum class Role {
    ADMIN,
    USER,
    MODERATOR,
}

fun safeRole(value: String): Role {
    return try {
        Role.valueOf(value)
    } catch (_: IllegalArgumentException) {
        Role.USER
    }
}
