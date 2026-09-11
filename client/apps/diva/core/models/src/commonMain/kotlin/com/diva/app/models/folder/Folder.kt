@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.folder

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Folder(
    val id: Uuid,
    val userId: Uuid,
    val name: String,
    val path: String,
    val parentId: Option<Uuid> = None,
    val scannedAt: Instant = Clock.System.now(),
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
    val deletedAt: Option<Instant> = None,
)
