package io.github.juevigrace.diva.lib.session.di

import io.github.juevigrace.diva.lib.database.session.SessionStorage
import io.github.juevigrace.diva.lib.session.data.SessionRepositoryImpl
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
        singleOf(::SessionStorageImpl) bind SessionStorage::class
        singleOf(::SessionRepositoryImpl) bind SessionRepository::class
        viewModelOf(::SessionViewModel)
    }
}
