plugins {
    id("divabuild.library-app-ui-shared")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)

            implementation(projects.core)
            implementation(projects.ui)

            implementation(projects.features.library)
            implementation(projects.features.search)
            implementation(projects.features.profile)
        }
    }
}
