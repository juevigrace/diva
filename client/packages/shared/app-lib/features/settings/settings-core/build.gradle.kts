plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.settings.settingsDatabase)
            implementation(projects.features.settings.settingsModels)

            implementation(libs.diva.network)
        }
    }
}
