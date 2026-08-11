package com.diva.user.di

import com.diva.database.user.UserStorage
import com.diva.database.user.actions.UserActionsStorage
import com.diva.database.user.permissions.UserPermissionsStorage
import com.diva.database.user.preferences.UserPreferencesStorage
import com.diva.user.api.client.UserApi
import com.diva.user.api.client.UserApiImpl
import com.diva.user.api.client.actions.UserActionsApi
import com.diva.user.api.client.actions.UserActionsApiImpl
import com.diva.user.api.client.devices.UserDevicesApi
import com.diva.user.api.client.devices.UserDevicesApiImpl
import com.diva.user.api.client.permissions.UserPermissionsApi
import com.diva.user.api.client.permissions.UserPermissionsApiImpl
import com.diva.user.api.client.preferences.UserPreferencesApi
import com.diva.user.api.client.preferences.UserPreferencesApiImpl
import com.diva.user.api.client.profile.UserProfileApi
import com.diva.user.api.client.profile.UserProfileApiImpl
import com.diva.user.api.client.status.UserStatusApi
import com.diva.user.api.client.status.UserStatusApiImpl
import com.diva.user.data.UserRepository
import com.diva.user.data.UserRepositoryImpl
import com.diva.user.data.actions.UserActionsRepository
import com.diva.user.data.actions.UserActionsRepositoryImpl
import com.diva.user.data.devices.UserDevicesRepository
import com.diva.user.data.devices.UserDevicesRepositoryImpl
import com.diva.user.data.permissions.UserPermissionsRepository
import com.diva.user.data.permissions.UserPermissionsRepositoryImpl
import com.diva.user.data.preferences.UserPreferencesRepository
import com.diva.user.data.preferences.UserPreferencesRepositoryImpl
import com.diva.user.data.profile.UserProfileRepository
import com.diva.user.data.profile.UserProfileRepositoryImpl
import com.diva.user.data.status.UserStatusRepository
import com.diva.user.data.status.UserStatusRepositoryImpl
import com.diva.user.database.UserStorageImpl
import com.diva.user.database.actions.UserActionsStorageImpl
import com.diva.user.database.permissions.UserPermissionsStorageImpl
import com.diva.user.database.preferences.UserPreferencesStorageImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun userModule(): Module {
    return module {
        singleOf(::UserStorageImpl) { bind<UserStorage>() }
        singleOf(::UserPermissionsStorageImpl) { bind<UserPermissionsStorage>() }
        singleOf(::UserPreferencesStorageImpl) { bind<UserPreferencesStorage>() }
        singleOf(::UserActionsStorageImpl) { bind<UserActionsStorage>() }

        singleOf(::UserApiImpl) { bind<UserApi>() }
        singleOf(::UserActionsApiImpl) { bind<UserActionsApi>() }
        singleOf(::UserPreferencesApiImpl) { bind<UserPreferencesApi>() }
        singleOf(::UserProfileApiImpl) { bind<UserProfileApi>() }
        singleOf(::UserStatusApiImpl) { bind<UserStatusApi>() }
        singleOf(::UserPermissionsApiImpl) { bind<UserPermissionsApi>() }
        singleOf(::UserDevicesApiImpl) { bind<UserDevicesApi>() }

        singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
        singleOf(::UserActionsRepositoryImpl) { bind<UserActionsRepository>() }
        singleOf(::UserPreferencesRepositoryImpl) { bind<UserPreferencesRepository>() }
        singleOf(::UserProfileRepositoryImpl) { bind<UserProfileRepository>() }
        singleOf(::UserStatusRepositoryImpl) { bind<UserStatusRepository>() }
        singleOf(::UserPermissionsRepositoryImpl) { bind<UserPermissionsRepository>() }
        singleOf(::UserDevicesRepositoryImpl) { bind<UserDevicesRepository>() }
    }
}
