package io.github.juevigrace.diva.lib.file.domain.services

import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.file.models.DeviceFile

private object WebThumbnailService : ThumbnailService {
    override suspend fun getThumbnail(entry: DeviceFile, sizePx: Int): Option<ByteArray> = None
}

actual fun createThumbnailService(): ThumbnailService = WebThumbnailService
