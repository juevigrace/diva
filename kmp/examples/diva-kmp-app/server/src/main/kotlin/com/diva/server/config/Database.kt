package com.diva.server.config

import com.diva.server.database.Seed
import io.ktor.server.application.Application
import org.koin.ktor.ext.inject

fun Application.configureDatabase() {
    val seed: Seed by inject()
    seed
        .addRootUser(
            username = environment.config.property("root.username").getString(),
            email = environment.config.property("root.email").getString(),
            password = environment.config.property("root.password").getString()
        )
        .seed()
}
