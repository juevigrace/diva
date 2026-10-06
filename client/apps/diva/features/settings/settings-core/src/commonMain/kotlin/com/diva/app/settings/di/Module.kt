package com.diva.app.settings.di

import com.diva.app.core.AppDatabase
import com.diva.app.settings.database.SettingsStorageImpl
import com.diva.app.settings.data.SettingsRepositoryImpl
import io.github.juevigrace.diva.lib.settings.domain.SettingsRepository
import io.github.juevigrace.diva.lib.settings.models.SettingsStorage
import io.github.juevigrace.diva.lib.settings.presentation.viewmodel.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun settingsModule(): Module {
    return module {
        single<SettingsStorage> { SettingsStorageImpl(get(qualifier = AppDatabase)) }

        singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class

        viewModelOf(::SettingsViewModel)
    }
}
