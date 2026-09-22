plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaLibCore)
            implementation(projects.divaLibDatabase)

            implementation(libs.diva.network)
        }
    }
}
