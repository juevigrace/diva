package com.diva.app.library.di

import com.diva.app.core.AppDatabase
import com.diva.app.library.data.LibraryRepositoryImpl
import com.diva.app.library.database.FavoriteStorage
import com.diva.app.library.database.FavoriteStorageImpl
import com.diva.app.library.domain.LibraryRepository
import com.diva.app.library.presentation.viewmodel.LibraryViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun libraryModule(): Module {
    return module {
        single<FavoriteStorage> { FavoriteStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::LibraryRepositoryImpl) bind LibraryRepository::class

        viewModelOf(::LibraryViewModel)
    }
}
