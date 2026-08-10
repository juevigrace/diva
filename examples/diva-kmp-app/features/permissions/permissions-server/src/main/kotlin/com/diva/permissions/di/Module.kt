package com.diva.permissions.di

import com.diva.database.permissions.PermissionsStorage
import com.diva.permissions.data.PermissionsService
import com.diva.permissions.data.PermissionsServiceImpl
import com.diva.permissions.database.PermissionsStorageImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun permissionsModule(): Module {
    return module {
        singleOf(::PermissionsStorageImpl) { bind<PermissionsStorage>() }
        singleOf(::PermissionsServiceImpl) { bind<PermissionsService>() }
    }
}
