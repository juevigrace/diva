plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.database)

            implementation(projects.core)
            implementation(projects.resources)
            implementation(projects.features.library.libraryDatabase)

            implementation(projects.features.media.mediaCore)
            implementation(projects.features.folder.folderCore)
            implementation(projects.features.player.playerCore)
            implementation(projects.features.server.serverCore)

            implementation(libs.diva.network)
        }
    }
}
