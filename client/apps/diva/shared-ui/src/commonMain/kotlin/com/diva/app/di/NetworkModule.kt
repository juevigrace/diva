package com.diva.app.di

import io.github.juevigrace.diva.network.client.DivaClient
import org.koin.core.module.Module
import org.koin.dsl.module

fun networkModule(): Module {
    return module {
        single<DivaClient> {
            DivaClient.create()
        }
    }
}
