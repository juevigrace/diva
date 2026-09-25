package com.diva.app.profile.di

import com.diva.app.profile.data.ProfileRepositoryImpl
import com.diva.app.profile.domain.ProfileRepository
import com.diva.app.profile.presentation.viewmodel.ProfileViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun profileModule(): Module {
    return module {
        singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class

        viewModelOf(::ProfileViewModel)
    }
}