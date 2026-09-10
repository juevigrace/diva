plugins {
    id("divabuild.library-app-ui-shared")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.ui)

            api(projects.core.resources)
            implementation(projects.core.models)
        }
    }
}
