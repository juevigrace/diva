plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaLibCore)
            implementation(projects.divaLibApiModels)

            implementation(projects.features.divaLibSession)
            implementation(libs.diva.network)

            implementation(projects.features.divaLibUserDatabase)
        }
    }
}
