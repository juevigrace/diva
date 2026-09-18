plugins {
    id("divabuild.library-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaLibCore)
            implementation(projects.divaLibModelsApi)
            implementation(projects.divaLibModelsCore)

            implementation(projects.features.divaLibSession)
            implementation(libs.diva.network)

            implementation(projects.features.divaLibUserDatabase)
        }
    }
}
