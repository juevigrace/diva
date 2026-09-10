package com.diva.app.features.profile.di

import com.diva.app.features.profile.data.ProfileRepositoryImpl
import com.diva.app.features.profile.domain.ProfileRepository
import com.diva.app.features.profile.presentation.viewmodel.ProfileViewModel
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