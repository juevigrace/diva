plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.file.fileDatabase)
            implementation(projects.features.file.fileModels)
        }
    }
}
