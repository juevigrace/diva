package com.diva.app.profile.di

import com.diva.app.profile.data.ProfileRepository
import com.diva.app.profile.data.ProfileRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun profileModule(): Module {
    return module {
        singleOf(::ProfileRepositoryImpl) { bind<ProfileRepository>() }
    }
}
