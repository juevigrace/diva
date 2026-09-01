package io.github.juevigrace.diva.lib.user.di

import io.github.juevigrace.diva.lib.user.data.UserRepositoryImpl
import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.lib.user.presentation.viewmodel.UserViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun userModule(): Module {
    return module {
        singleOf(::UserRepositoryImpl) bind UserRepository::class

        viewModelOf(::UserViewModel)
    }
}
