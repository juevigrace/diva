@file:DivaJsExport

package com.diva.app.file.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlin.time.Clock

/**
 * A regular file on the device filesystem. Same keying convention as
 * [Directory]: [id] is an opaque UUID, [path] the absolute device path.
 */
data class DeviceFile(
    val id: String,
    val name: String,
    val path: String,
    val parent: Option<Directory> = None,
    val mimeType: Option<String> = None,
    val sizeBytes: Long,
    val lastModified: Long,
    val scannedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val deletedAt: Option<Long> = None,
)
