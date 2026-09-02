plugins {
    id("divabuild.library-app")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaLibModelsApi)
        }
    }
}
