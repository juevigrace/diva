package io.github.juevigrace.diva.lib.file.domain.services

import org.koin.mp.KoinPlatformTools

actual fun createThumbnailService(): ThumbnailService {
    val context = org.koin.mp.KoinPlatformTools.defaultContext().get().get<android.content.Context>()
    return ThumbnailServiceImpl(context)
}
