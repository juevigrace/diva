package com.diva.app.features.server.di

import com.diva.app.database.server.ServerStorage
import com.diva.app.features.server.data.ServerRepositoryImpl
import com.diva.app.features.server.database.ServerStorageImpl
import com.diva.app.features.server.domain.ServerRepository
import com.diva.app.features.server.presentation.viewmodel.ServerViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun serverModule(): Module {
    return module {
        singleOf(::ServerStorageImpl) bind ServerStorage::class

        singleOf(::ServerRepositoryImpl) bind ServerRepository::class

        viewModelOf(::ServerViewModel)
    }
}