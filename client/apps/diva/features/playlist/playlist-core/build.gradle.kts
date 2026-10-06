plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.playlist.playlistDatabase)

            implementation(projects.features.collection.collectionModels)

            implementation(libs.diva.network)
        }
    }
}
