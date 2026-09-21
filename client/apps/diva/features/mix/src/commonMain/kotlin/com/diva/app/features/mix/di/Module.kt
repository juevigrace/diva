package com.diva.app.features.mix.di

import com.diva.app.core.AppDatabase
import com.diva.app.database.collection.mix.MixMetadataStorage
import com.diva.app.features.mix.data.MixRepositoryImpl
import com.diva.app.features.mix.database.MixMetadataStorageImpl
import com.diva.app.features.mix.domain.MixRepository
import com.diva.app.features.mix.presentation.viewmodel.MixViewModel
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