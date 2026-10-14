@file:DivaJsExport

package com.diva.app.file.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option

/**
 * A regular file on the device filesystem. All shared entry info lives in
 * [meta]; only file-specific fields are declared here.
 */
data class DeviceFile(
    override val meta: EntryMeta,
    val sizeBytes: Long,
    val mimeType: Option<String> = None,
) : FsEntry
