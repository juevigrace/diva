package com.diva.app.features.library.di

import com.diva.app.database.playback.FavoriteStorage
import com.diva.app.features.library.data.LibraryRepositoryImpl
import com.diva.app.features.library.database.FavoriteStorageImpl
import com.diva.app.features.library.domain.LibraryRepository
import com.diva.app.features.library.presentation.viewmodel.LibraryViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun libraryModule(): Module {
    return module {
        singleOf(::FavoriteStorageImpl) bind FavoriteStorage::class

        singleOf(::LibraryRepositoryImpl) bind LibraryRepository::class

        viewModelOf(::LibraryViewModel)
    }
}