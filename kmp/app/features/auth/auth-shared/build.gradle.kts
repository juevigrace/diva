plugins {
    id("divabuild.library-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.app.core.database)

            api(projects.app.core.ui)

            api(libs.diva.network.client)
        }
    }
}
