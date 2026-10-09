plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.ui)

            implementation(projects.features.settings.settingsCore)
            implementation(projects.features.settings.settingsModels)

            implementation(projects.features.library.libraryCore)
            implementation(projects.features.folder.folderCore)
            implementation(projects.features.search.searchCore)

            implementation(projects.features.profile.profileCore)
            implementation(projects.features.player.playerCore)

            implementation(projects.features.collection.collectionCore)
            implementation(projects.features.collection.collectionModels)
            implementation(projects.features.mix.mixCore)
            implementation(projects.features.playlist.playlistCore)

            implementation(libs.diva.lib.user.core)
            implementation(libs.diva.lib.user.models)
        }
    }
}
