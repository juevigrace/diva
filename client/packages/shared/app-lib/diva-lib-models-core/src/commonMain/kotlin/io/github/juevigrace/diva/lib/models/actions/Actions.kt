package io.github.juevigrace.diva.lib.models.actions

enum class Actions {
    USER_VERIFICATION,
    PASSWORD_RESET,
    EMAIL_UPDATE,
    USERNAME_UPDATE,
    PHONE_UPDATE,
    USER_RESTORE,
    UNKNOWN,
}

fun safeActionsValueOf(value: String): Actions {
    return try {
        Actions.valueOf(value)
    } catch (_: IllegalArgumentException) {
        Actions.UNKNOWN
    }
}
