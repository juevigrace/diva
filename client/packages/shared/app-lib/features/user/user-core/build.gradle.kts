plugins {
    id("divabuild.app-lib-ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.ui)

            implementation(projects.features.auth.authModels)

            implementation(projects.features.session.sessionCore)
            implementation(projects.features.session.sessionModels)

            implementation(projects.features.user.userDatabase)
            implementation(projects.features.user.userModels)

            implementation(libs.diva.network)
        }
    }
}
