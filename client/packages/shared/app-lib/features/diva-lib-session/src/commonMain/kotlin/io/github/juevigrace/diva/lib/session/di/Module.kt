package io.github.juevigrace.diva.lib.session.di

import io.github.juevigrace.diva.lib.database.SharedDatabase
import io.github.juevigrace.diva.lib.database.session.SessionStorage
import io.github.juevigrace.diva.lib.session.data.SessionRepositoryImpl
import io.github.juevigrace.diva.lib.session.data.api.client.SessionsApi
import io.github.juevigrace.diva.lib.session.data.api.client.SessionsApiImpl
import io.github.juevigrace.diva.lib.session.database.SessionStorageImpl
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.presentation.viewmodel.SessionViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun sessionModule(): Module {
    return module {
        singleOf(::SessionsApiImpl) bind SessionsApi::class
        single<SessionStorage> { SessionStorageImpl(get(qualifier = SharedDatabase)) }
        singleOf(::SessionRepositoryImpl) bind SessionRepository::class
        viewModelOf(::SessionViewModel)
    }
}
