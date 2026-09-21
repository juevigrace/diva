@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.media

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock

data class MediaMetadata(
    val mediaId: String,
    val album: String = "",
    val artist: String = "",
    val genre: String = "",
    val year: Option<Int> = None,
    val trackNumber: Option<Int> = None,
    val discNumber: Option<Int> = None,
    val coverUri: String = "",
    val lyrics: String = "",
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
)
