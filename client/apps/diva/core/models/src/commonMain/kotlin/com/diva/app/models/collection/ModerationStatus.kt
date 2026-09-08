package com.diva.app.models.collection

enum class ModerationStatus {
    PENDING,
    APPROVED,
    REJECTED,
    HIDDEN,
    UNSPECIFIED,
}

fun safeModerationStatus(value: String): ModerationStatus {
    return try {
        ModerationStatus.valueOf(value)
    } catch (_: IllegalArgumentException) {
        ModerationStatus.UNSPECIFIED
    }
}
