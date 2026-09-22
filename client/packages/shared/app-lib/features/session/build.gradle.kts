plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaLibCore)
            implementation(projects.divaLibApiModels)

            implementation(libs.diva.network)

            implementation(projects.features.divaLibSessionDatabase)
        }
    }
}
