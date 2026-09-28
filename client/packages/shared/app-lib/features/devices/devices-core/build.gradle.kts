plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.devices.devicesDatabase)
            implementation(projects.features.devices.devicesModels)

            implementation(libs.diva.network)
        }
    }
}
