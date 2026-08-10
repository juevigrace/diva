package com.diva.media.di

import com.diva.database.media.MediaStorage
import com.diva.database.media.tag.TagStorage
import com.diva.media.api.client.MediaClient
import com.diva.media.api.client.MediaClientImpl
import com.diva.media.api.client.tag.TagClient
import com.diva.media.api.client.tag.TagClientImpl
import com.diva.media.data.MediaRepository
import com.diva.media.data.MediaRepositoryImpl
import com.diva.media.data.tag.TagRepository
import com.diva.media.data.tag.TagRepositoryImpl
import com.diva.media.database.MediaStorageImpl
import com.diva.media.database.tag.TagStorageImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun mediaModule(): Module {
    return module {
        singleOf(::TagStorageImpl) { bind<TagStorage>() }
        singleOf(::TagClientImpl) { bind<TagClient>() }
        singleOf(::TagRepositoryImpl) { bind<TagRepository>() }

        singleOf(::MediaStorageImpl) { bind<MediaStorage>() }
        singleOf(::MediaClientImpl) { bind<MediaClient>() }
        singleOf(::MediaRepositoryImpl) { bind<MediaRepository>() }
    }
}
