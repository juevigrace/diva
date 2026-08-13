plugins {
    id("divabuild.library-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.features.user)

            implementation(projects.app.core.ui)
        }
    }
}