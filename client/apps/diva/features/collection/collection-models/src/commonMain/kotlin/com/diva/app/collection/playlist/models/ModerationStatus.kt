@file:DivaJsExport

package com.diva.app.collection.playlist.models

import io.github.juevigrace.diva.core.DivaJsExport

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
