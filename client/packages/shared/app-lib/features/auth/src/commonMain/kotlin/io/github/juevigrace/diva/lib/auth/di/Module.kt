package io.github.juevigrace.diva.lib.auth.di

import io.github.juevigrace.diva.lib.auth.data.AuthRepositoryImpl
import io.github.juevigrace.diva.lib.auth.domain.AuthRepository
import io.github.juevigrace.diva.lib.auth.presentation.viewmodel.ForgotViewModel
import io.github.juevigrace.diva.lib.auth.presentation.viewmodel.SignInViewModel
import io.github.juevigrace.diva.lib.auth.presentation.viewmodel.SignUpViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun authModule(): Module {
    return module {
        singleOf(::AuthRepositoryImpl) bind AuthRepository::class

        viewModelOf(::SignInViewModel)
        viewModelOf(::SignUpViewModel)
        viewModelOf(::ForgotViewModel)
    }
}
