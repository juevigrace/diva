package io.github.juevigrace.diva.lib.onboarding.di

import io.github.juevigrace.diva.lib.onboarding.data.OnboardingRepositoryImpl
import io.github.juevigrace.diva.lib.onboarding.domain.OnboardingRepository
import io.github.juevigrace.diva.lib.onboarding.presentation.viewmodel.OnboardingViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun onboardingModule(): Module {
    return module {
        singleOf(::OnboardingRepositoryImpl) bind OnboardingRepository::class

        viewModelOf(::OnboardingViewModel)
    }
}
