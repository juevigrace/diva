@file:DivaJsExport

package com.diva.app.file.models

import io.github.juevigrace.diva.core.DivaJsExport

/**
 * Lightweight pointer to another entry, used for [EntryMeta.parent]. Holds just
 * enough to identify and display the target without embedding it, so entry graphs
 * can never become cyclic through parent/children links.
 */
data class EntryRef(
    val id: String,
    val path: String,
    val name: String,
)
