plugins {
    id("divabuild.app-library")
    id("divabuild.serialization")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.core.database)

            implementation(projects.app.features.auth.authShared)
            implementation(projects.app.features.auth.session)
            implementation(projects.app.features.user)

            implementation(libs.diva.network.client)
        }
    }
}
