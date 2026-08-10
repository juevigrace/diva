package com.diva.verification.di

import com.diva.database.session.SessionStorage
import com.diva.session.database.SessionStorageImpl
import com.diva.verification.api.client.VerificationNetworkClient
import com.diva.verification.api.client.VerificationNetworkClientImpl
import com.diva.verification.data.VerificationRepository
import com.diva.verification.data.VerificationRepositoryImpl
import com.diva.verification.presentation.viewmodel.VerificationViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun verificationModule(): Module {
    return module {
        singleOf(::SessionStorageImpl) { bind<SessionStorage>() }

        singleOf(::VerificationNetworkClientImpl) { bind<VerificationNetworkClient>() }

        singleOf(::VerificationRepositoryImpl) { bind<VerificationRepository>() }

        viewModelOf(::VerificationViewModel)
    }
}
