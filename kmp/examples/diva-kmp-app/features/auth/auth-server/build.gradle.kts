plugins {
    id("divabuild.library-server")
}

dependencies {
    implementation(projects.core.database.databaseServer)
    implementation(projects.core.modelsServer)
    implementation(projects.core.server.util)

    implementation(projects.features.user.userServer)
    implementation(projects.features.verification.verificationServer)

    implementation(projects.features.session.sessionServer)

    implementation(libs.koin.core)
    implementation(libs.koin.ktor)

    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.request.validation)
}
