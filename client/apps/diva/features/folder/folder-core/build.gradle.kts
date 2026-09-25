plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.database)

            implementation(projects.core)
            implementation(projects.features.folder.folderDatabase)

            implementation(libs.diva.network)
        }
    }
}
