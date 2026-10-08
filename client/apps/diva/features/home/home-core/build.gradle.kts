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

            implementation(projects.features.feed.feedCore)
            implementation(projects.features.library.libraryCore)
            implementation(projects.features.search.searchCore)
            implementation(projects.features.profile.profileCore)
            implementation(projects.features.folder.folderCore)
            implementation(projects.features.media.mediaModels)
            implementation(projects.features.player.playerCore)
        }
    }
}
