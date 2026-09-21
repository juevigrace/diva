@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.collection

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport

enum class VisibilityType {
    PUBLIC,
    PRIVATE,
    FRIENDS,
    UNSPECIFIED,
}

fun safeVisibilityType(value: String): VisibilityType {
    return try {
        VisibilityType.valueOf(value)
    } catch (_: IllegalArgumentException) {
        VisibilityType.UNSPECIFIED
    }
}
