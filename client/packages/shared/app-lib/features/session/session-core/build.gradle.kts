plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.features.session.sessionModels)

            implementation(libs.diva.network)

            implementation(projects.features.session.sessionDatabase)
        }
    }
}
