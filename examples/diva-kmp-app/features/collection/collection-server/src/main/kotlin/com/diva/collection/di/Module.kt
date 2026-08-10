package com.diva.collection.di

import com.diva.collection.data.CollectionService
import com.diva.collection.data.CollectionServiceImpl
import com.diva.collection.data.playlist.PlaylistService
import com.diva.collection.data.playlist.PlaylistServiceImpl
import com.diva.collection.database.CollectionStorageImpl
import com.diva.collection.database.playlist.PlaylistStorageImpl
import com.diva.database.collection.CollectionStorage
import com.diva.database.collection.playlist.PlaylistStorage
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun collectionModule(): Module {
    return module {
        singleOf(::CollectionStorageImpl) { bind<CollectionStorage>() }
        singleOf(::CollectionServiceImpl) { bind<CollectionService>() }
        singleOf(::PlaylistStorageImpl) { bind<PlaylistStorage>() }
        singleOf(::PlaylistServiceImpl) { bind<PlaylistService>() }
    }
}
