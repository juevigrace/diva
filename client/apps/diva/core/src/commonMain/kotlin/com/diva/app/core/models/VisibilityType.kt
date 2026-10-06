@file:DivaJsExport

package com.diva.app.core.models

import io.github.juevigrace.diva.core.DivaJsExport

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
