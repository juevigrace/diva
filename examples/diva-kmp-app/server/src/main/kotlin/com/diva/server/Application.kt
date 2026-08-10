package com.diva.server

import com.diva.server.config.configureAdministration
import com.diva.server.config.configureDatabase
import com.diva.server.config.configureFrameworks
import com.diva.server.config.configureHTTP
import com.diva.server.config.configureRouting
import com.diva.server.config.configureSecurity
import com.diva.server.config.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureFrameworks()
    configureAdministration()
    configureSerialization()
    configureSecurity()
    configureHTTP()
    configureRouting()
    configureDatabase()
}
