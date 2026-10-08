plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.settings.settingsDatabase)
            implementation(projects.features.settings.settingsModels)

            api(libs.diva.lib.settings.core)
            implementation(libs.diva.lib.session.core)
            implementation(libs.diva.lib.session.models)

            implementation(libs.diva.network)
        }
    }
}
