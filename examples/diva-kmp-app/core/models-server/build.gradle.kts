plugins {
    id("divabuild.library-server")
}

dependencies {
    api(projects.core.models)
    api(projects.core.models.modelsApi)

    implementation(libs.ktor.server.core)
}
