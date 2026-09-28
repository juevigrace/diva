plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.database)
            implementation(projects.ui)

            implementation(libs.diva.network)
        }
    }
}
