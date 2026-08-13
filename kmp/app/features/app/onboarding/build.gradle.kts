plugins {
    id("divabuild.library-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.core.ui)

            implementation(projects.app.features.user)
        }
    }
}
