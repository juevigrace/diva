plugins {
    id("divabuild.library-app-ui-shared")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)

            implementation(projects.core.models)
            implementation(projects.core.ui)
        }
    }
}
