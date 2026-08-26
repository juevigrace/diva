plugins {
    id("divabuild.library-app-desktop")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedUI)
        }
    }
}
