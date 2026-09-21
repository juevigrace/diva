plugins {
    id("divabuild.library-app-ui-shared")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.session)

            implementation(projects.core)
            implementation(projects.resources)

            implementation(projects.features.media)
            implementation(projects.features.folder)
            implementation(projects.features.collection)
        }
    }
}