plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)

            implementation(projects.core)
            implementation(projects.ui)

            implementation(projects.features.library.libraryCore)
            implementation(projects.features.search.searchCore)
            implementation(projects.features.profile.profileCore)
        }
    }
}
