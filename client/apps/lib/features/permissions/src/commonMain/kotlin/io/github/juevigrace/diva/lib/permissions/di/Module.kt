package io.github.juevigrace.diva.lib.permissions.di

import io.github.juevigrace.diva.lib.database.permissions.PermissionsStorage
import io.github.juevigrace.diva.lib.permissions.data.PermissionsRepositoryImpl
import io.github.juevigrace.diva.lib.permissions.database.PermissionsStorageImpl
import io.github.juevigrace.diva.lib.permissions.domain.PermissionsRepository
import io.github.juevigrace.diva.lib.permissions.presentation.viewmodel.PermissionsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun permissionsModule(): Module = module {
    singleOf(::PermissionsStorageImpl) bind PermissionsStorage::class
    singleOf(::PermissionsRepositoryImpl) bind PermissionsRepository::class
    viewModelOf(::PermissionsViewModel)
}
