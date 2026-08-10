plugins {
    id("divabuild.library")
    id("divabuild.targets-web")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.models.modelsApi)

            api(projects.core.models.modelsShared)
        }
    }
}
