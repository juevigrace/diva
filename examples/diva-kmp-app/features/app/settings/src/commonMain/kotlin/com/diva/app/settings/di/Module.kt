package com.diva.app.settings.di

import com.diva.app.settings.data.SettingsRepository
import com.diva.app.settings.data.SettingsRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun settingsModule(): Module {
    return module {
        singleOf(::SettingsRepositoryImpl) { bind<SettingsRepository>() }
    }
}
