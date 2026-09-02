package io.github.juevigrace.diva.lib.devices.di

import io.github.juevigrace.diva.lib.database.devices.DevicesStorage
import io.github.juevigrace.diva.lib.devices.data.DevicesRepositoryImpl
import io.github.juevigrace.diva.lib.devices.database.DevicesStorageImpl
import io.github.juevigrace.diva.lib.devices.domain.DevicesRepository
import io.github.juevigrace.diva.lib.devices.presentation.viewmodel.DevicesViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun devicesModule(): Module = module {
    singleOf(::DevicesStorageImpl) bind DevicesStorage::class
    singleOf(::DevicesRepositoryImpl) bind DevicesRepository::class
    viewModelOf(::DevicesViewModel)
}
