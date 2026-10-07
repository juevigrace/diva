plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.folder.folderDatabase)
            implementation(projects.features.folder.folderModels)
            implementation(projects.features.collection.collectionModels)

            implementation(projects.features.media.mediaModels)
            implementation(projects.features.media.mediaCore)
            implementation(projects.features.player.playerCore)

            implementation(libs.diva.lib.session.core)
            implementation(libs.diva.lib.session.models)

            implementation(libs.diva.network)
        }
    }
}
