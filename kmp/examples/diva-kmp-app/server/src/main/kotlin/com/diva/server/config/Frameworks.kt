package com.diva.server.config

import com.diva.server.di.appModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    val config = environment.config
    install(Koin) {
        slf4jLogger()
        modules(appModule(config))
    }
}
