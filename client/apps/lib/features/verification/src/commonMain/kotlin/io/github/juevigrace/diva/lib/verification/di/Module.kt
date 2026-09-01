package io.github.juevigrace.diva.lib.verification.di

import io.github.juevigrace.diva.lib.verification.data.VerificationRepositoryImpl
import io.github.juevigrace.diva.lib.verification.domain.VerificationRepository
import io.github.juevigrace.diva.lib.verification.presentation.viewmodel.VerificationViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun verificationModule(): Module {
    return module {
        singleOf(::VerificationRepositoryImpl) bind VerificationRepository::class

        viewModelOf(::VerificationViewModel)
    }
}
