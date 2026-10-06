@file:DivaJsExport

package com.diva.app.folder.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.user.models.User
import kotlin.time.Clock

data class Folder(
    val id: String,
    val user: User,
    val name: String,
    val path: String,
    val parent: Option<Folder> = None,
    val scannedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val deletedAt: Option<Long> = None,
)
