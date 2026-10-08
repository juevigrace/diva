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
            api(projects.core)
            implementation(projects.database)
            implementation(projects.ui)

            implementation(projects.features.home.homeCore)
            implementation(projects.features.feed.feedCore)
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
            implementation(projects.features.settings.settingsCore)
            api(projects.features.settings.settingsModels)

            implementation(libs.diva.lib.auth.core)
            implementation(libs.diva.lib.devices.core)
            implementation(libs.diva.lib.user.core)
            implementation(libs.diva.lib.session.core)
            implementation(libs.diva.lib.session.models)
            implementation(libs.diva.lib.settings.core)
            implementation(libs.diva.lib.permissions.core)
            implementation(libs.diva.lib.onboarding.core)
            implementation(libs.diva.lib.verification.core)

            implementation(libs.diva.network)
        }
    }
}
