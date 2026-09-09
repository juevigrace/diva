package com.diva.app.models.media

import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class MediaMetadata(
    val mediaId: Uuid,
    val album: String = "",
    val artist: String = "",
    val genre: String = "",
    val year: Option<Int> = None,
    val trackNumber: Option<Int> = None,
    val discNumber: Option<Int> = None,
    val coverUri: String = "",
    val lyrics: String = "",
    val updatedAt: Instant = Clock.System.now(),
)
