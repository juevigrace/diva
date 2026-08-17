plugins {
    id("divabuild.app-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.models.modelsApi)

            api(projects.core.models.modelsShared)
        }
    }
}
