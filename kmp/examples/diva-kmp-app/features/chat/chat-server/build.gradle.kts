plugins {
    id("divabuild.library-server")
}

dependencies {
    implementation(projects.core.database.databaseServer)
    implementation(projects.core.modelsServer)
    implementation(projects.core.server.util)

    implementation(libs.koin.core)
    implementation(libs.koin.ktor)

    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.request.validation)
}
