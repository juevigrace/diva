@file:DivaJsExport

package io.github.juevigrace.diva.lib.core.models

import io.github.juevigrace.diva.core.DivaJsExport

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
