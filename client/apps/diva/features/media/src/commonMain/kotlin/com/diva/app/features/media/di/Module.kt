package com.diva.app.features.media.di

import com.diva.app.core.AppDatabase
import com.diva.app.database.media.MediaMetadataStorage
import com.diva.app.database.media.MediaStorage
import com.diva.app.database.media.MediaTagStorage
import com.diva.app.database.media.TagStorage
import com.diva.app.features.media.data.MediaMetadataRepositoryImpl
import com.diva.app.features.media.data.MediaRepositoryImpl
import com.diva.app.features.media.data.TagRepositoryImpl
import com.diva.app.features.media.database.MediaMetadataStorageImpl
import com.diva.app.features.media.database.MediaStorageImpl
import com.diva.app.features.media.database.MediaTagStorageImpl
import com.diva.app.features.media.database.TagStorageImpl
import com.diva.app.features.media.domain.MediaMetadataRepository
import com.diva.app.features.media.domain.MediaRepository
import com.diva.app.features.media.domain.TagRepository
import com.diva.app.features.media.presentation.viewmodel.MediaViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun mediaModule(): Module {
    return module {
        single<MediaStorage> { MediaStorageImpl(get(qualifier = AppDatabase)) }
        single<MediaMetadataStorage> { MediaMetadataStorageImpl(get(qualifier = AppDatabase)) }
        single<TagStorage> { TagStorageImpl(get(qualifier = AppDatabase)) }
        single<MediaTagStorage> { MediaTagStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::MediaRepositoryImpl) bind MediaRepository::class
        singleOf(::MediaMetadataRepositoryImpl) bind MediaMetadataRepository::class
        singleOf(::TagRepositoryImpl) bind TagRepository::class

        viewModelOf(::MediaViewModel)
    }
}