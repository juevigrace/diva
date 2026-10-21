@file:DivaJsExport

package io.github.juevigrace.diva.lib.file.models

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
    val mimeType: Option<MimeType> = None,
    val category: FileCategory = FileCategory.OTHER,
    val contentUri: Option<String> = None,
) : FsEntry
