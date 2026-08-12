plugins {
    id("divabuild.library-server")
}

dependencies {
    implementation(projects.core.modelsServer)
    api(projects.core.server.mail)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.kbcrypt)
}
