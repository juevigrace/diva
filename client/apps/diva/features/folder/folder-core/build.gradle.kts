plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.folder.folderDatabase)
            implementation(projects.features.folder.folderModels)

            implementation(projects.features.media.mediaModels)

            implementation(libs.diva.network)
        }
    }
}
