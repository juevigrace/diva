package com.diva.app.di

import com.diva.app.presentation.viewmodel.SystemViewModel
import com.diva.app.ui.di.uiModule
import io.github.juevigrace.diva.lib.system.di.systemModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun appModule(): Module = module {
    viewModel { SystemViewModel(get()) }

    includes(
        uiModule(),
        systemModule(),
    )
}
