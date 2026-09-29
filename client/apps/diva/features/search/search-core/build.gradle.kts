plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.search.searchModels)

            implementation(projects.features.collection.collectionCore)
            implementation(projects.features.folder.folderCore)
            implementation(projects.features.media.mediaCore)

            implementation(libs.diva.lib.session.core)
            implementation(libs.diva.lib.session.models)
        }
    }
}
