plugins {
    id("divabuild.library-server")
}

dependencies {
    implementation(projects.core.modelsServer)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.html)

    implementation(libs.resend)
}
