import io.ktor.plugin.features.DockerPortMapping
import io.ktor.plugin.features.DockerPortMappingProtocol

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    application
}

group = "com.diva.server"
version = libs.versions.app.version.name

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation(libs.diva.core)

    implementation(projects.core.server.util)
    implementation(projects.core.database.databaseServer)
    implementation(projects.core.modelsServer)

    implementation(projects.features.auth.authServer)
    implementation(projects.features.verification.verificationServer)
    implementation(projects.features.chat.chatServer)
    implementation(projects.features.collection.collectionServer)
    implementation(projects.features.media.mediaServer)
    implementation(projects.features.permissions.permissionsServer)
    implementation(projects.features.session.sessionServer)
    implementation(projects.features.social.socialServer)
    implementation(projects.features.user.userServer)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.sse)
    implementation(libs.ktor.server.request.validation)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.forwarded.header)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.server.caching.headers)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.config.yaml)

    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_21)
        localImageName.set("diva-server")
        imageTag.set(libs.versions.app.version.name)
        portMappings.set(
            listOf(
                DockerPortMapping(
                    outsideDocker = 5000,
                    insideDocker = 5000,
                    protocol = DockerPortMappingProtocol.TCP
                )
            )
        )
    }
    fatJar {
        archiveFileName.set("diva-server.jar")
    }
}
