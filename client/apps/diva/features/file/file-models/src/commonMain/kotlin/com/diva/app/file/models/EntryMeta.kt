@file:DivaJsExport

package com.diva.app.file.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlin.time.Clock

/**
 * Shared metadata for every [FsEntry]. [id] is an opaque UUID assigned when the
 * entry enters the cache; [path] is the absolute device path (globally unique).
 * [lastModified] is the device filesystem mtime; the remaining timestamps are
 * cache-local.
 */
data class EntryMeta(
    val id: String,
    val name: String,
    val path: String,
    val lastModified: Long,
    val parent: Option<EntryRef> = None,
    val scannedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val deletedAt: Option<Long> = None,
)
