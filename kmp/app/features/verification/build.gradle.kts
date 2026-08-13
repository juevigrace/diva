plugins {
    id("divabuild.library-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.core.database)

            implementation(projects.app.core.ui)

            implementation(projects.app.features.auth.session)

            implementation(projects.app.features.auth.forgot)

            implementation(projects.app.features.user)

            implementation(libs.diva.network.client)
        }
    }
}
