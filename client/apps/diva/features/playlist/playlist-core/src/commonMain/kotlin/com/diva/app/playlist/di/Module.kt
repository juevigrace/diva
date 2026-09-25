package com.diva.app.playlist.di

import com.diva.app.core.AppDatabase
import com.diva.app.playlist.data.PlaylistRepositoryImpl
import com.diva.app.playlist.database.PlaylistContributorStorage
import com.diva.app.playlist.database.PlaylistContributorStorageImpl
import com.diva.app.playlist.database.PlaylistMetadataStorage
import com.diva.app.playlist.database.PlaylistMetadataStorageImpl
import com.diva.app.playlist.database.PlaylistSuggestionsStorage
import com.diva.app.playlist.database.PlaylistSuggestionsStorageImpl
import com.diva.app.playlist.domain.PlaylistRepository
import com.diva.app.playlist.presentation.viewmodel.PlaylistViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun playlistModule(): Module {
    return module {
        single<PlaylistMetadataStorage> { PlaylistMetadataStorageImpl(get(qualifier = AppDatabase)) }
        single<PlaylistContributorStorage> { PlaylistContributorStorageImpl(get(qualifier = AppDatabase)) }
        single<PlaylistSuggestionsStorage> { PlaylistSuggestionsStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::PlaylistRepositoryImpl) bind PlaylistRepository::class

        viewModelOf(::PlaylistViewModel)
    }
}
