@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.collection

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport

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
