plugins {
    id("divabuild.diva-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.media.mediaCore)
            implementation(projects.features.media.mediaModels)
        }
    }
}
