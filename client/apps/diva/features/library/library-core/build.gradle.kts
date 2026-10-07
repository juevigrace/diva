plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.library.libraryDatabase)

            implementation(projects.features.media.mediaCore)
            implementation(projects.features.media.mediaModels)

            implementation(projects.features.folder.folderCore)
            implementation(projects.features.folder.folderModels)

            implementation(projects.features.player.playerCore)

            implementation(projects.features.server.serverCore)

            implementation(libs.diva.lib.session.core)
            implementation(libs.diva.lib.session.models)
        }
    }
}
