plugins {
    id("divabuild.library-ui")
    id("divabuild.serialization")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.core.database)

            implementation(projects.app.core.ui)

            implementation(projects.app.features.auth.session)

            implementation(libs.diva.network.client)
        }
    }
}
