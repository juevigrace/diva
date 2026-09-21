@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.collection

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport

enum class CollectionType {
    ALBUM,
    PLAYLIST,
    MIX,
    FAVORITES,
    FEATURED,
    TRENDING,
    UNSPECIFIED,
}

fun safeCollectionType(value: String): CollectionType {
    return try {
        CollectionType.valueOf(value)
    } catch (_: IllegalArgumentException) {
        CollectionType.UNSPECIFIED
    }
}
