package com.diva.permission.di

import com.diva.permission.api.client.PermissionsApi
import com.diva.permission.api.client.PermissionsApiImpl
import com.diva.permission.data.PermissionsRepository
import com.diva.permission.data.PermissionsRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun permissionModule(): Module {
    return module {
        singleOf(::PermissionsApiImpl) { bind<PermissionsApi>() }

        singleOf(::PermissionsRepositoryImpl) { bind<PermissionsRepository>() }
    }
}
