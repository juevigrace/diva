plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.session)

            implementation(projects.core)
            implementation(projects.resources)

            implementation(projects.features.media.mediaCore)
            implementation(projects.features.folder.folderCore)
            implementation(projects.features.collection.collectionCore)
        }
    }
}
