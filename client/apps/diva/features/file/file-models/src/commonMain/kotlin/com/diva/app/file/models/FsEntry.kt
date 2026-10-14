@file:DivaJsExport

package com.diva.app.file.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.Option

/**
 * A filesystem entry on the device. Either a [Directory] or a [DeviceFile].
 * All shared identity, lineage and metadata lives in [meta]; implementations
 * only declare their own unique fields.
 */
sealed interface FsEntry {
    val meta: EntryMeta

    val id: String get() = meta.id
    val name: String get() = meta.name
    val path: String get() = meta.path
    val parent: Option<EntryRef> get() = meta.parent
    val lastModified: Long get() = meta.lastModified
    val scannedAt: Long get() = meta.scannedAt
    val createdAt: Long get() = meta.createdAt
    val updatedAt: Long get() = meta.updatedAt
    val deletedAt: Option<Long> get() = meta.deletedAt

    val ref: EntryRef get() = EntryRef(id, path, name)
}
