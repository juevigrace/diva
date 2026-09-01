package io.github.juevigrace.diva.lib.settings.di

import io.github.juevigrace.diva.lib.settings.data.SettingsRepositoryImpl
import io.github.juevigrace.diva.lib.settings.domain.SettingsRepository
import io.github.juevigrace.diva.lib.settings.presentation.viewmodel.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun settingsModule(): Module {
    return module {
        singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class

        viewModelOf(::SettingsViewModel)
    }
}
