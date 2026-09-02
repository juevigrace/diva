package io.github.juevigrace.diva.lib.user.di

import io.github.juevigrace.diva.lib.database.user.UserStorage
import io.github.juevigrace.diva.lib.database.user.actions.UserActionsStorage
import io.github.juevigrace.diva.lib.database.user.devices.UserDevicesStorage
import io.github.juevigrace.diva.lib.database.user.permissions.UserPermissionsStorage
import io.github.juevigrace.diva.lib.database.user.preferences.UserPreferencesStorage
import io.github.juevigrace.diva.lib.database.user.profile.UserProfileStorage
import io.github.juevigrace.diva.lib.database.user.state.UserStateStorage
import io.github.juevigrace.diva.lib.user.data.UserActionsRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserDevicesRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserPermissionsRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserPreferencesRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserProfileRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserRepositoryImpl
import io.github.juevigrace.diva.lib.user.data.UserStateRepositoryImpl
import io.github.juevigrace.diva.lib.user.database.UserActionsStorageImpl
import io.github.juevigrace.diva.lib.user.database.UserDevicesStorageImpl
import io.github.juevigrace.diva.lib.user.database.UserPermissionsStorageImpl
import io.github.juevigrace.diva.lib.user.database.UserPreferencesStorageImpl
import io.github.juevigrace.diva.lib.user.database.UserProfileStorageImpl
import io.github.juevigrace.diva.lib.user.database.UserStateStorageImpl
import io.github.juevigrace.diva.lib.user.database.UserStorageImpl
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
        singleOf(::UserStorageImpl) bind UserStorage::class
        singleOf(::UserStateStorageImpl) bind UserStateStorage::class
        singleOf(::UserActionsStorageImpl) bind UserActionsStorage::class
        singleOf(::UserDevicesStorageImpl) bind UserDevicesStorage::class
        singleOf(::UserProfileStorageImpl) bind UserProfileStorage::class
        singleOf(::UserPermissionsStorageImpl) bind UserPermissionsStorage::class
        singleOf(::UserPreferencesStorageImpl) bind UserPreferencesStorage::class

        singleOf(::UserRepositoryImpl) bind UserRepository::class
        singleOf(::UserStateRepositoryImpl) bind UserStateRepository::class
        singleOf(::UserActionsRepositoryImpl) bind UserActionsRepository::class
        singleOf(::UserDevicesRepositoryImpl) bind UserDevicesRepository::class
        singleOf(::UserProfileRepositoryImpl) bind UserProfileRepository::class
        singleOf(::UserPermissionsRepositoryImpl) bind UserPermissionsRepository::class
        singleOf(::UserPreferencesRepositoryImpl) bind UserPreferencesRepository::class

        viewModelOf(::UserViewModel)
    }
}
