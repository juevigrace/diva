@file:DivaJsExport

package com.diva.app.file.models

import io.github.juevigrace.diva.core.DivaJsExport

/**
 * A directory on the device filesystem. Its immediate children are described by
 * [entries]; all shared entry info lives in [meta].
 */
data class Directory(
    override val meta: EntryMeta,
    val entries: List<FsEntry> = emptyList(),
) : FsEntry
