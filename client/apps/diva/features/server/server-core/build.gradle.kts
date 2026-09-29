plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.server.serverDatabase)
            implementation(projects.features.server.serverModels)

            implementation(libs.diva.network)
        }
    }
}
