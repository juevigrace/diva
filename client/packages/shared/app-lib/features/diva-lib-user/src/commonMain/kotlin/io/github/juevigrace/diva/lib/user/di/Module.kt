package io.github.juevigrace.diva.lib.user.di

import io.github.juevigrace.diva.lib.core.SharedDatabase
import io.github.juevigrace.diva.lib.user.data.UserActionsRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserDevicesRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserPermissionsRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserPreferencesRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserProfileRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserStateRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.api.client.UserActionsApi
import io.github.juevigrace.diva.lib.user.data.api.client.UserActionsApiImpl
import io.github.juevigrace.diva.lib.user.data.api.client.UserApi
import io.github.juevigrace.diva.lib.user.data.api.client.UserApiImpl
import io.github.juevigrace.diva.lib.user.data.api.client.UserDevicesApi
import io.github.juevigrace.diva.lib.user.data.api.client.UserDevicesApiImpl
import io.github.juevigrace.diva.lib.user.data.api.client.UserPermissionsApi
import io.github.juevigrace.diva.lib.user.data.api.client.UserPermissionsApiImpl
import io.github.juevigrace.diva.lib.user.data.api.client.UserPreferencesApi
import io.github.juevigrace.diva.lib.user.data.api.client.UserPreferencesApiImpl
import io.github.juevigrace.diva.lib.user.data.api.client.UserProfileApi
import io.github.juevigrace.diva.lib.user.data.api.client.UserProfileApiImpl
import io.github.juevigrace.diva.lib.user.data.api.client.UserSessionsApi
import io.github.juevigrace.diva.lib.user.data.api.client.UserSessionsApiImpl
import io.github.juevigrace.diva.lib.user.data.api.client.UserStateApi
import io.github.juevigrace.diva.lib.user.data.api.client.UserStateApiImpl
import io.github.juevigrace.diva.lib.user.database.UserStorage
import io.github.juevigrace.diva.lib.user.database.UserStorageImpl
import io.github.juevigrace.diva.lib.user.database.actions.UserActionsStorage
import io.github.juevigrace.diva.lib.user.database.actions.UserActionsStorageImpl
import io.github.juevigrace.diva.lib.user.database.devices.UserDevicesStorage
import io.github.juevigrace.diva.lib.user.database.devices.UserDevicesStorageImpl
import io.github.juevigrace.diva.lib.user.database.permissions.UserPermissionsStorage
import io.github.juevigrace.diva.lib.user.database.permissions.UserPermissionsStorageImpl
import io.github.juevigrace.diva.lib.user.database.preferences.UserPreferencesStorage
import io.github.juevigrace.diva.lib.user.database.preferences.UserPreferencesStorageImpl
import io.github.juevigrace.diva.lib.user.database.profile.UserProfileStorage
import io.github.juevigrace.diva.lib.user.database.profile.UserProfileStorageImpl
import io.github.juevigrace.diva.lib.user.database.state.UserStateStorage
import io.github.juevigrace.diva.lib.user.database.state.UserStateStorageImpl
import io.github.juevigrace.diva.lib.user.domain.UserActionsRepository
import io.github.juevigrace.diva.lib.user.domain.UserDevicesRepository
import io.github.juevigrace.diva.lib.user.domain.UserPermissionsRepository
import io.github.juevigrace.diva.lib.user.domain.UserPreferencesRepository
import io.github.juevigrace.diva.lib.user.domain.UserProfileRepository
import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.lib.user.domain.UserStateRepository
import io.github.juevigrace.diva.lib.user.presentation.viewmodel.UserViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun userModule(): Module {
    return module {
        single<UserStorage> { UserStorageImpl(get(qualifier = SharedDatabase)) }
        single<UserStateStorage> { UserStateStorageImpl(get(qualifier = SharedDatabase)) }
        single<UserActionsStorage> { UserActionsStorageImpl(get(qualifier = SharedDatabase)) }
        single<UserDevicesStorage> { UserDevicesStorageImpl(get(qualifier = SharedDatabase)) }
        single<UserProfileStorage> { UserProfileStorageImpl(get(qualifier = SharedDatabase)) }
        single<UserPermissionsStorage> { UserPermissionsStorageImpl(get(qualifier = SharedDatabase)) }
        single<UserPreferencesStorage> { UserPreferencesStorageImpl(get(qualifier = SharedDatabase)) }

        singleOf(::UserRepositoryImpl) bind UserRepository::class
        singleOf(::UserStateRepositoryImpl) bind UserStateRepository::class
        singleOf(::UserActionsRepositoryImpl) bind UserActionsRepository::class
        singleOf(::UserDevicesRepositoryImpl) bind UserDevicesRepository::class
        singleOf(::UserProfileRepositoryImpl) bind UserProfileRepository::class
        singleOf(::UserPermissionsRepositoryImpl) bind UserPermissionsRepository::class
        singleOf(::UserPreferencesRepositoryImpl) bind UserPreferencesRepository::class

        singleOf(::UserApiImpl) bind UserApi::class
        singleOf(::UserStateApiImpl) bind UserStateApi::class
        singleOf(::UserActionsApiImpl) bind UserActionsApi::class
        singleOf(::UserDevicesApiImpl) bind UserDevicesApi::class
        singleOf(::UserProfileApiImpl) bind UserProfileApi::class
        singleOf(::UserPermissionsApiImpl) bind UserPermissionsApi::class
        singleOf(::UserPreferencesApiImpl) bind UserPreferencesApi::class
        singleOf(::UserSessionsApiImpl) bind UserSessionsApi::class

        viewModelOf(::UserViewModel)
    }
}
