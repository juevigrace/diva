package io.github.juevigrace.diva.lib.permissions.di

import io.github.juevigrace.diva.lib.core.SharedDatabase
import io.github.juevigrace.diva.lib.permissions.data.PermissionsRepositoryImpl
import io.github.juevigrace.diva.lib.permissions.data.api.client.PermissionsApi
import io.github.juevigrace.diva.lib.permissions.data.api.client.PermissionsApiImpl
import io.github.juevigrace.diva.lib.permissions.database.PermissionsStorage
import io.github.juevigrace.diva.lib.permissions.database.PermissionsStorageImpl
import io.github.juevigrace.diva.lib.permissions.domain.PermissionsRepository
import io.github.juevigrace.diva.lib.permissions.presentation.viewmodel.PermissionsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun permissionsModule(): Module = module {
    singleOf(::PermissionsApiImpl) bind PermissionsApi::class
    single<PermissionsStorage> { PermissionsStorageImpl(get(qualifier = SharedDatabase)) }
    singleOf(::PermissionsRepositoryImpl) bind PermissionsRepository::class
    viewModelOf(::PermissionsViewModel)
}
