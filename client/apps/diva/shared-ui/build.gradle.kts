plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.database)
            implementation(libs.diva.lib.ui)

            implementation(libs.diva.lib.auth)
            implementation(libs.diva.lib.user)
            implementation(libs.diva.lib.session)
            implementation(libs.diva.lib.settings)

            implementation(libs.diva.network)

            implementation(projects.core)
            implementation(projects.apiModels)
            implementation(projects.database)
            implementation(projects.ui)
            implementation(projects.features.home.homeCore)
            implementation(projects.features.server.serverCore)
            implementation(projects.features.media.mediaCore)
            implementation(projects.features.folder.folderCore)
            implementation(projects.features.collection.collectionCore)
            implementation(projects.features.playlist.playlistCore)
            implementation(projects.features.mix.mixCore)
            implementation(projects.features.player.playerCore)
            implementation(projects.features.library.libraryCore)
            implementation(projects.features.search.searchCore)
            implementation(projects.features.profile.profileCore)
        }
    }
}
