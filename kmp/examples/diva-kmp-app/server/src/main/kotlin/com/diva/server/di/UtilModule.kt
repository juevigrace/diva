package com.diva.server.di

import com.diva.mail.KMail
import com.diva.mail.KMailConfig
import com.diva.util.JwtHelper
import io.ktor.server.config.ApplicationConfig
import org.koin.core.module.Module
import org.koin.dsl.module

fun utilModule(config: ApplicationConfig): Module {
    return module {
        single {
            JwtHelper(
                secret = config.property("jwt.secret").getString(),
                issuer = config.property("jwt.issuer").getString(),
                audience = config.property("jwt.audience").getString(),
            )
        }

        single {
            KMail(
                config = KMailConfig(
                    apiKey = config.property("mail.apiKey").getString(),
                    fromEmail = config.property("mail.fromEmail").getString(),
                )
            )
        }
    }
}
