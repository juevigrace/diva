@file:DivaJsExport

package io.github.juevigrace.diva.lib.user.models

import io.github.juevigrace.diva.core.DivaJsExport

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
