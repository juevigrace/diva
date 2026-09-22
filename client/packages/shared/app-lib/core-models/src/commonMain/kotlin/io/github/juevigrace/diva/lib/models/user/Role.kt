@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.user

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport

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
