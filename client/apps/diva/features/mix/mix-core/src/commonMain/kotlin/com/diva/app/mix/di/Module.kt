package com.diva.app.mix.di

import com.diva.app.core.AppDatabase
import com.diva.app.mix.data.MixRepositoryImpl
import com.diva.app.mix.database.MixMetadataStorage
import com.diva.app.mix.database.MixMetadataStorageImpl
import com.diva.app.mix.domain.MixRepository
import com.diva.app.mix.presentation.viewmodel.MixViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun mixModule(): Module {
    return module {
        single<MixMetadataStorage> { MixMetadataStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::MixRepositoryImpl) bind MixRepository::class

        viewModelOf(::MixViewModel)
    }
}
