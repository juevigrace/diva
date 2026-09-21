@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.folder

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock

data class Folder(
    val id: String,
    val userId: String,
    val name: String,
    val path: String,
    val parentId: Option<String> = None,
    val scannedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val deletedAt: Option<Long> = None,
)
