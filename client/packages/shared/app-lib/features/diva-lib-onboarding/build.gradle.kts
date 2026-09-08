plugins {
    id("divabuild.library-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaLibCore)
            implementation(projects.divaLibDatabase)
            implementation(projects.divaLibModelsCore)

            implementation(libs.diva.network)
        }
    }
}
