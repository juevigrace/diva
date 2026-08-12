package com.diva.permissions.di

import com.diva.database.permissions.PermissionsStorage
import com.diva.permissions.api.client.PermissionsClient
import com.diva.permissions.api.client.PermissionsClientImpl
import com.diva.permissions.data.PermissionsRepository
import com.diva.permissions.data.PermissionsRepositoryImpl
import com.diva.permissions.database.PermissionsStorageImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun permissionsModule(): Module {
    return module {
        singleOf(::PermissionsClientImpl) { bind<PermissionsClient>() }
        singleOf(::PermissionsStorageImpl) { bind<PermissionsStorage>() }
        singleOf(::PermissionsRepositoryImpl) { bind<PermissionsRepository>() }
    }
}
