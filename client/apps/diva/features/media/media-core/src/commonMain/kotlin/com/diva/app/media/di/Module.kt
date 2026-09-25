package com.diva.app.media.di

import com.diva.app.core.AppDatabase
import com.diva.app.media.data.MediaMetadataRepositoryImpl
import com.diva.app.media.data.MediaRepositoryImpl
import com.diva.app.media.data.TagRepositoryImpl
import com.diva.app.media.database.MediaMetadataStorage
import com.diva.app.media.database.MediaMetadataStorageImpl
import com.diva.app.media.database.MediaStorage
import com.diva.app.media.database.MediaStorageImpl
import com.diva.app.media.database.MediaTagStorage
import com.diva.app.media.database.MediaTagStorageImpl
import com.diva.app.media.database.TagStorage
import com.diva.app.media.database.TagStorageImpl
import com.diva.app.media.domain.MediaMetadataRepository
import com.diva.app.media.domain.MediaRepository
import com.diva.app.media.domain.TagRepository
import com.diva.app.media.presentation.viewmodel.MediaViewModel
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
