plugins {
    id("divabuild.library-app-ui-shared")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.database)

            implementation(projects.core)
            implementation(projects.features.libraryDatabase)

            implementation(projects.features.media)
            implementation(projects.features.folder)
            implementation(projects.features.player)
            implementation(projects.features.server)

            implementation(libs.diva.network)
        }
    }
}
