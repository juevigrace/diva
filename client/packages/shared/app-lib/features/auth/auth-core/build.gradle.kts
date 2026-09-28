plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.auth.authModels)
            implementation(projects.features.session.sessionModels)

            implementation(libs.diva.network)
        }
    }
}
