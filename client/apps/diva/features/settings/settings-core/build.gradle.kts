plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.settings.settingsDatabase)
            implementation(projects.features.settings.settingsModels)

            implementation(libs.diva.lib.settings.core)

            implementation(libs.diva.network)
        }
    }
}
