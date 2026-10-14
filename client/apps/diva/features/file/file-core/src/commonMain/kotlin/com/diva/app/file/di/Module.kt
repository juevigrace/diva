package com.diva.app.file.di

import com.diva.app.core.AppDatabase
import com.diva.app.file.data.DirectoryRepositoryImpl
import com.diva.app.file.database.DirectoryStorage
import com.diva.app.file.database.DirectoryStorageImpl
import com.diva.app.file.domain.DirectoryRepository
import com.diva.app.file.presentation.viewmodel.DirectoryViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun fileModule(): Module {
    return module {
        includes(platformFileSystemServiceModule())

        single<DirectoryStorage> { DirectoryStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::DirectoryRepositoryImpl) bind DirectoryRepository::class

        viewModelOf(::DirectoryViewModel)
    }
}
