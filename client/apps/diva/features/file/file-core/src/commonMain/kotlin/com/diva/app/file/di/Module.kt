package com.diva.app.file.di

import com.diva.app.core.AppDatabase
import com.diva.app.file.data.FileRepositoryImpl
import com.diva.app.file.database.EntryStorage
import com.diva.app.file.database.EntryStorageImpl
import com.diva.app.file.domain.FileRepository
import com.diva.app.file.presentation.viewmodel.FilesViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun fileModule(): Module {
    return module {
        includes(platformFileSystemServiceModule())

        single<EntryStorage> { EntryStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::FileRepositoryImpl) bind FileRepository::class

        viewModelOf(::FilesViewModel)
    }
}
