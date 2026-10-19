package io.github.juevigrace.diva.lib.file.di

import io.github.juevigrace.diva.lib.core.SharedDatabase
import io.github.juevigrace.diva.lib.file.data.FileRepositoryImpl
import io.github.juevigrace.diva.lib.file.database.EntryStorage
import io.github.juevigrace.diva.lib.file.database.EntryStorageImpl
import io.github.juevigrace.diva.lib.file.domain.FileRepository
import io.github.juevigrace.diva.lib.file.presentation.viewmodel.FilesViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun fileModule(): Module {
    return module {
        includes(platformFileSystemServiceModule())

        single<EntryStorage> { EntryStorageImpl(get(qualifier = SharedDatabase)) }

        singleOf(::FileRepositoryImpl) bind FileRepository::class

        viewModelOf(::FilesViewModel)
    }
}
