plugins {
    id("divabuild.library-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaLibCore)
            implementation(projects.divaLibModelsApi)
            implementation(projects.divaLibModelsCore)

            implementation(libs.diva.network)

            implementation(projects.features.divaLibPermissionsDatabase)
        }
    }
}
