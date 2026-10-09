package com.diva.app.profile.di

import com.diva.app.profile.presentation.viewmodel.ProfileViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun profileModule(): Module {
    return module {
        viewModelOf(::ProfileViewModel)
    }
}
