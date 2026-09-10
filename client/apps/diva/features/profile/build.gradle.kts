plugins {
    id("divabuild.library-app-ui-shared")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.diva.lib.core)
            implementation(libs.diva.lib.user)
            implementation(libs.diva.lib.session)

            implementation(projects.core.models)
        }
    }
}