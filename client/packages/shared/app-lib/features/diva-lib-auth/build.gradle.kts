plugins {
    id("divabuild.library-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaLibCore)
            implementation(projects.divaLibDatabase)
            implementation(projects.divaLibModelsApi)
            implementation(projects.divaLibModelsCore)
            implementation(projects.divaLibUi)

            implementation(libs.diva.network)
        }
    }
}
