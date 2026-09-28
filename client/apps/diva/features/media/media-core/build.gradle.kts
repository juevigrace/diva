plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.database)

            implementation(projects.core)
            implementation(projects.features.media.mediaDatabase)

            api(projects.features.media.mediaModels)

            implementation(libs.diva.network)
        }
    }
}
