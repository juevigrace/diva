package com.diva.app.file.di

import com.diva.app.core.AppDatabase
import com.diva.app.file.data.FileRepositoryImpl
import com.diva.app.file.database.FileStorage
import com.diva.app.file.database.FileStorageImpl
import com.diva.app.file.domain.FileRepository
import com.diva.app.file.presentation.viewmodel.DirectoryViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun fileModule(): Module {
    return module {
        includes(platformFileSystemServiceModule())

        single<FileStorage> { FileStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::FileRepositoryImpl) bind FileRepository::class

        viewModelOf(::DirectoryViewModel)
    }
}
