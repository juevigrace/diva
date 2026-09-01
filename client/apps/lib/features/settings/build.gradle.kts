plugins {
    id("divabuild.library-app-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.database)

            implementation(libs.diva.network)
        }
    }
}
