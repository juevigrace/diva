plugins {
    id("divabuild.library-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.database)
            implementation(projects.features.auth)
            implementation(projects.features.user)

            implementation(libs.diva.network)
        }
    }
}
