plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.settings.settingsModels)

            implementation(projects.features.session.sessionCore)
            implementation(projects.features.session.sessionModels)

            implementation(libs.diva.network)
        }
    }
}
