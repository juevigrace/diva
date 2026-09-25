package com.diva.app.server.di

import com.diva.app.core.AppDatabase
import com.diva.app.server.data.ServerRepositoryImpl
import com.diva.app.server.database.ServerStorage
import com.diva.app.server.database.ServerStorageImpl
import com.diva.app.server.domain.ServerRepository
import com.diva.app.server.presentation.viewmodel.ServerViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun serverModule(): Module {
    return module {
        single<ServerStorage> { ServerStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::ServerRepositoryImpl) bind ServerRepository::class

        viewModelOf(::ServerViewModel)
    }
}
