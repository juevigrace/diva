@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.user

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport

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
