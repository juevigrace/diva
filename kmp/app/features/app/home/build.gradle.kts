plugins {
    id("divabuild.library-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.features.user)

            implementation(projects.app.core.ui)

            implementation(projects.app.features.app.library)
            implementation(projects.app.features.app.feed)
            implementation(projects.app.features.app.creation)
            implementation(projects.app.features.app.profile)
        }
    }
}
