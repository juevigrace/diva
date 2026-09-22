plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaLibCore)
            implementation(projects.divaLibDatabase)
            implementation(projects.divaLibApiModels)
            implementation(projects.divaLibUi)

            implementation(libs.diva.network)
        }
    }
}
