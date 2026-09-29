plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.media.mediaDatabase)
            implementation(projects.features.media.mediaModels)

            implementation(libs.diva.network)
        }
    }
}
