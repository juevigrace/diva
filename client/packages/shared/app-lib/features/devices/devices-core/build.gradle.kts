plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.features.devices.devicesModels)

            implementation(libs.diva.network)

            implementation(projects.features.devices.devicesDatabase)
        }
    }
}
