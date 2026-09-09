package com.diva.app.features.playlist.di

import com.diva.app.database.collection.playlist.PlaylistContributorStorage
import com.diva.app.database.collection.playlist.PlaylistMetadataStorage
import com.diva.app.database.collection.playlist.PlaylistSuggestionsStorage
import com.diva.app.features.playlist.data.PlaylistRepositoryImpl
import com.diva.app.features.playlist.database.PlaylistContributorStorageImpl
import com.diva.app.features.playlist.database.PlaylistMetadataStorageImpl
import com.diva.app.features.playlist.database.PlaylistSuggestionsStorageImpl
import com.diva.app.features.playlist.domain.PlaylistRepository
import com.diva.app.features.playlist.presentation.viewmodel.PlaylistViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun playlistModule(): Module {
    return module {
        singleOf(::PlaylistMetadataStorageImpl) bind PlaylistMetadataStorage::class
        singleOf(::PlaylistContributorStorageImpl) bind PlaylistContributorStorage::class
        singleOf(::PlaylistSuggestionsStorageImpl) bind PlaylistSuggestionsStorage::class

        singleOf(::PlaylistRepositoryImpl) bind PlaylistRepository::class

        viewModelOf(::PlaylistViewModel)
    }
}