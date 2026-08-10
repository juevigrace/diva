package com.diva.media.di

import com.diva.database.media.MediaStorage
import com.diva.database.media.tag.TagStorage
import com.diva.media.data.MediaService
import com.diva.media.data.MediaServiceImpl
import com.diva.media.data.TagService
import com.diva.media.data.TagServiceImpl
import com.diva.media.database.MediaStorageImpl
import com.diva.media.database.tag.TagStorageImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun mediaModule(): Module {
    return module {
        singleOf(::TagStorageImpl) { bind<TagStorage>() }
        singleOf(::TagServiceImpl) { bind<TagService>() }

        singleOf(::MediaStorageImpl) { bind<MediaStorage>() }
        singleOf(::MediaServiceImpl) { bind<MediaService>() }
    }
}
