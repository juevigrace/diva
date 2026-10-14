@file:DivaJsExport

package com.diva.app.file.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlin.time.Clock

/**
 * A directory on the device filesystem. [id] is an opaque UUID assigned when the
 * directory enters the cache; [path] is the absolute device path (globally unique).
 */
data class Directory(
    val id: String,
    val name: String,
    val path: String,
    val parent: Option<Directory> = None,
    val scannedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val deletedAt: Option<Long> = None,
)
