package com.diva.verification.di

import com.diva.verification.api.handler.VerificationHandler
import com.diva.verification.api.handler.VerificationHandlerImpl
import com.diva.verification.data.VerificationService
import com.diva.verification.data.VerificationServiceImpl
import com.diva.verification.database.VerificationStorage
import com.diva.verification.database.VerificationStorageImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun verificationModule(): Module {
    return module {
        singleOf(::VerificationStorageImpl) { bind<VerificationStorage>() }
        singleOf(::VerificationServiceImpl) { bind<VerificationService>() }
        singleOf(::VerificationHandlerImpl) { bind<VerificationHandler>() }
    }
}
