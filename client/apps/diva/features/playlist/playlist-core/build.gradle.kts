plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.database)

            implementation(projects.core)
            implementation(projects.features.playlist.playlistDatabase)

            api(projects.features.collection.collectionModels)

            implementation(libs.diva.network)
        }
    }
}
