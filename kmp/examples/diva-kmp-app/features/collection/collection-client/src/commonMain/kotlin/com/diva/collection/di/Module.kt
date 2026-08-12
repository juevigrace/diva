package com.diva.collection.di

import com.diva.collection.api.client.CollectionClient
import com.diva.collection.api.client.CollectionClientImpl
import com.diva.collection.api.client.playlist.PlaylistClient
import com.diva.collection.api.client.playlist.PlaylistClientImpl
import com.diva.collection.data.CollectionRepository
import com.diva.collection.data.CollectionRepositoryImpl
import com.diva.collection.data.playlist.PlaylistRepository
import com.diva.collection.data.playlist.PlaylistRepositoryImpl
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
        singleOf(::CollectionClientImpl) { bind<CollectionClient>() }
        singleOf(::CollectionStorageImpl) { bind<CollectionStorage>() }
        singleOf(::CollectionRepositoryImpl) { bind<CollectionRepository>() }

        singleOf(::PlaylistClientImpl) { bind<PlaylistClient>() }
        singleOf(::PlaylistStorageImpl) { bind<PlaylistStorage>() }
        singleOf(::PlaylistRepositoryImpl) { bind<PlaylistRepository>() }
    }
}
