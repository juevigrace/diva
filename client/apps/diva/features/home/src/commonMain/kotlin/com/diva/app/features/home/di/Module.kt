package com.diva.app.features.home.di

import com.diva.app.features.home.presentation.viewmodel.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun homeModule(): Module {
    return module {
        viewModelOf(::HomeViewModel)
    }
}
