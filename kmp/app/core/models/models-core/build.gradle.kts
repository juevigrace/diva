plugins {
    id("divabuild.app-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.app.core.models.modelsApi)

            api(projects.app.core.models.modelsShared)
        }
    }
}
