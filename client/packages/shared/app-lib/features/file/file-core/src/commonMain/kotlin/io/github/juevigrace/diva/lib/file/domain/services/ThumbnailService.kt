package io.github.juevigrace.diva.lib.file.domain.services

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.file.models.DeviceFile

interface ThumbnailService {
    suspend fun getThumbnail(entry: DeviceFile, sizePx: Int): Option<ByteArray>
}

expect fun createThumbnailService(): ThumbnailService
