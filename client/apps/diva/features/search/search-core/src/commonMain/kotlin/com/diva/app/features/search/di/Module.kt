package com.diva.app.features.search.di

import com.diva.app.features.search.data.SearchRepositoryImpl
import com.diva.app.features.search.domain.SearchRepository
import com.diva.app.features.search.presentation.viewmodel.SearchViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun searchModule(): Module {
    return module {
        singleOf(::SearchRepositoryImpl) bind SearchRepository::class

        viewModelOf(::SearchViewModel)
    }
}