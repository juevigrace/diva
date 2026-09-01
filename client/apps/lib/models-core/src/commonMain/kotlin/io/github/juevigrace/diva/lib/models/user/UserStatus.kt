package io.github.juevigrace.diva.lib.models.user

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
